package it.unimib.letsdrink.ui.profile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Category;
import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.ui.drinks.CategoryAdapter;
import it.unimib.letsdrink.ui.drinks.CocktailAdapter;
import it.unimib.letsdrink.ui.drinks.CocktailDetailFragment;
import it.unimib.letsdrink.ui.drinks.CocktailsCategoryFragment;
import it.unimib.letsdrink.ui.drinks.FirebaseDBCategories;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    //private ProfileViewModel profileViewModel;
    private TextView mUserNameCustom;
    private TextView mEmailCustom;
    private TextView mPlaceholder;
    private FloatingActionButton mAddDrinks;

    User user = new User();
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseDBCustomDrink mfirebaseDBCustomDrink;

    private CustomDrinkAdapter customDrinkAdapter;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        ActionBar actionBar= ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("Profilo");
        actionBar.setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings_item:
                /*this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);*/
                Navigation.findNavController(getView())
                        .navigate(R.id.action_profileFragment_to_tempSettingsFragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mfirebaseDBCustomDrink = new FirebaseDBCustomDrink(mAuth.getCurrentUser().getUid());

        mPlaceholder = view.findViewById(R.id.text_profile_not_logged_in);
        mPlaceholder.setText(R.string.value_no_custom_drinks);

        mUserNameCustom = view.findViewById(R.id.text_profile_user_name);
        RecyclerView recyclerView = view.findViewById(R.id.profile_recycler);

        FirebaseDBCustomDrink db = new FirebaseDBCustomDrink(mAuth.getCurrentUser().getUid());

        db.readCocktails(new FirebaseDBCustomDrink.DataStatus() {
            @Override
            public void dataIsLoaded(List<Cocktail> listOfCustomDrink) {
                if(listOfCustomDrink.size() == 0) {
                    mPlaceholder.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    customDrinkAdapter = new CustomDrinkAdapter(listOfCustomDrink, getContext());
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    recyclerView.setAdapter(customDrinkAdapter);

                    customDrinkAdapter.setOnItemClickListener(new CustomDrinkAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Fragment cocktailDetail = CustomDrinkDetailFragment.newInstance(listOfCustomDrink.get(position).getName(), listOfCustomDrink.get(position).getMethod(),
                                    listOfCustomDrink.get(position).getIngredients(), listOfCustomDrink.get(position).getImageUrl());

                            Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_customDrinkDetailFragment);
                        }

                        @Override
                        public void onDeleteClick(int position, View v) {

                            db.deleteCustomDrink(listOfCustomDrink.get(position), new FirebaseDBCustomDrink.DataStatus() {
                                @Override
                                public void dataIsLoaded(List<Cocktail> listOfCustomDrinkCocktail) {
                                    customDrinkAdapter.setListOfCocktails(listOfCustomDrinkCocktail);
                                    recyclerView.getRecycledViewPool().clear();
                                    customDrinkAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });
                }

            }
        });

        user.setUserID(mAuth.getUid());
        DocumentReference documentReference = mFirestore.collection("Utenti").document(user.getUserID());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    mUserNameCustom.setText(documentSnapshot.getString("userName"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ProfileFragment", "OnFailure mUserName");
            }
        });

        mEmailCustom = view.findViewById(R.id.text_profile_email);
        mEmailCustom.setText(mAuth.getCurrentUser().getEmail());

        mAddDrinks = view.findViewById(R.id.floating_action_button_profile_add_drink);
        mAddDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_customDrinkFragment);
            }
        });



    }

    /*public CustomDrinkAdapter getAdapter() {

        return customDrinkAdapter;
    }*/

}