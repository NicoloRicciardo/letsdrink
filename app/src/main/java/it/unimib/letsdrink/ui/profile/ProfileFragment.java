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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import de.hdodenhof.circleimageview.CircleImageView;
import it.unimib.letsdrink.R;
import it.unimib.letsdrink.adapters.CustomDrinkAdapter;
import it.unimib.letsdrink.domain.User;
import it.unimib.letsdrink.firebaseDB.FirebaseDBCustomDrink;

public class ProfileFragment extends Fragment {

    private TextView mUserNameCustom;
    private TextView mPlaceholder, mYourDrinks;
    private CircleImageView mUserImage;
    User user = new User();
    private FirebaseAuth mAuth;
    private StorageReference mStorageReference;
    private CustomDrinkAdapter customDrinkAdapter;

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
        if (id == R.id.settings_item) {
            Navigation.findNavController(getView())
                    .navigate(R.id.action_profileFragment_to_tempSettingsFragment);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        mPlaceholder = view.findViewById(R.id.text_profile_not_logged_in);
        mPlaceholder.setText(R.string.value_no_custom_drinks);
        mYourDrinks = view.findViewById(R.id.text_profile_your_drinks);
        mUserImage = view.findViewById(R.id.image_profile);
        setUserImage();

        mUserNameCustom = view.findViewById(R.id.text_profile_user_name);
        RecyclerView recyclerView = view.findViewById(R.id.profile_recycler);

        FirebaseDBCustomDrink db = new FirebaseDBCustomDrink(mAuth.getCurrentUser().getUid());

        db.readCocktails(listOfCustomDrink -> {
            if(listOfCustomDrink.size() == 0) {
                mPlaceholder.setVisibility(View.VISIBLE);
                mYourDrinks.setVisibility(View.INVISIBLE);
            } else {
                mYourDrinks.setVisibility(View.VISIBLE);
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
                        new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                                .setTitle("Eliminare il drink")
                                .setMessage("Sei sicuro di voler eliminare il tuo drink?/nÈ un'azione irreversibile.")
                                .setPositiveButton("Conferma", (dialog, which) -> db.deleteCustomDrink(listOfCustomDrink.get(position), listOfCustomDrinkCocktail -> {
                                    customDrinkAdapter.setListOfCocktails(listOfCustomDrinkCocktail);
                                    recyclerView.getRecycledViewPool().clear();
                                    customDrinkAdapter.notifyDataSetChanged();
                                    Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_self);
                                }))
                                .setNegativeButton("Esci", null)
                                .show();
                    }
                });
            }

        });

        user.setUserID(mAuth.getUid());
        DocumentReference documentReference = mFirestore.collection("Utenti").document(user.getUserID());
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()) {
                mUserNameCustom.setText(documentSnapshot.getString("userName"));
            }
        }).addOnFailureListener(e -> Log.d("ProfileFragment", "OnFailure mUserName"));

        TextView mEmailCustom = view.findViewById(R.id.text_profile_email);
        mEmailCustom.setText(mAuth.getCurrentUser().getEmail());

        FloatingActionButton mAddDrinks = view.findViewById(R.id.floating_action_button_profile_add_drink);
        mAddDrinks.setOnClickListener(v -> Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_customDrinkFragment));

    }

    private void setUserImage() {
        StorageReference fileRef = mStorageReference
                .child("UserImage/" + mAuth.getCurrentUser().getUid() + "/profile.jpg");
        fileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(mUserImage));
    }

}