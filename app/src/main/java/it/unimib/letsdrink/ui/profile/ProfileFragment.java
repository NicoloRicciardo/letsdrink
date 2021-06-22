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

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import it.unimib.letsdrink.R;
import it.unimib.letsdrink.adapters.CustomDrinkAdapter;
import it.unimib.letsdrink.domain.User;
import it.unimib.letsdrink.firebaseDB.FirebaseDBCustomDrink;

//fragment del profilo
public class ProfileFragment extends Fragment {

    private TextView mUserNameCustom, mPlaceholder, mYourDrinks;
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
        assert actionBar != null;
        actionBar.setTitle(R.string.title_profile);
        //non mette la freccia per tornare indietro
        actionBar.setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

        return root;
    }

    //crea una toolbar con l'icona dell'ingranaggio (preso da xml)
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    //gestisce il click dell'icona nella toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings_item) {
            //manda al fragment delle impostazioni
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_profileFragment_to_settingsFragment);
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
        //textview mostrata nel caso in cui l'utente non abbia creato alcun drink
        mPlaceholder.setText(R.string.value_no_custom_drinks);
        mYourDrinks = view.findViewById(R.id.text_profile_your_drinks);
        mUserImage = view.findViewById(R.id.image_profile);
        //setta l'immagine (se é stata cambiata) altrimenti mette quella di default
        setUserImage();

        mUserNameCustom = view.findViewById(R.id.text_profile_user_name);
        RecyclerView recyclerView = view.findViewById(R.id.profile_recycler);

        FirebaseDBCustomDrink db = new FirebaseDBCustomDrink(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        db.readCocktails(listOfCustomDrink -> {
            //se l'utente non ha creato alcun drink
            if(listOfCustomDrink.size() == 0) {
                mPlaceholder.setVisibility(View.VISIBLE);
                mYourDrinks.setVisibility(View.INVISIBLE);
                //se l'utente ha creato almeno un drink
            } else {
                mYourDrinks.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                customDrinkAdapter = new CustomDrinkAdapter(listOfCustomDrink, getContext());
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recyclerView.setAdapter(customDrinkAdapter);

                customDrinkAdapter.setOnItemClickListener(new CustomDrinkAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        CustomDrinkDetailFragment.newInstance(listOfCustomDrink.get(position).getName(), listOfCustomDrink.get(position).getMethod(),
                                listOfCustomDrink.get(position).getIngredients(), listOfCustomDrink.get(position).getImageUrl());

                        //visualizzazione del detail fragment del custom drink
                        Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_customDrinkDetailFragment);
                    }

                    //al click del cestino
                    @Override
                    public void onDeleteClick(int position, View v) {
                        //viene mostrata una alert dialog che chiede la conferma dell'eliminazione del custom drink
                        new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                                .setTitle(R.string.title_delete_custom_drink)
                                .setMessage(R.string.message_delete_custom_drink)
                                .setPositiveButton(R.string.button_confirm, (dialog, which) -> db.deleteCustomDrink(listOfCustomDrink.get(position), listOfCustomDrinkCocktail -> {
                                    //viene riaggiornata la lista dei custom drink
                                    customDrinkAdapter.setListOfCocktails(listOfCustomDrinkCocktail);
                                    recyclerView.getRecycledViewPool().clear();
                                    customDrinkAdapter.notifyDataSetChanged();
                                    //refresh per vedere i drink effettivamente presenti nel profilo
                                    Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_self);
                                }))
                                .setNegativeButton(R.string.button_exit, null)
                                .show();
                    }
                });
            }

        });

        //setta la textview del nome utente accedento a firestore e prendendo lo username inserito dall'utente
        user.setUserID(mAuth.getUid());
        DocumentReference documentReference = mFirestore.collection("Utenti").document(user.getUserID());
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()) {
                mUserNameCustom.setText(documentSnapshot.getString("userName"));
            }
        }).addOnFailureListener(e -> Log.d("ProfileFragment", "OnFailure mUserName"));

        //setta la textview della email presente su FirebaseAuth
        TextView mEmailCustom = view.findViewById(R.id.text_profile_email);
        mEmailCustom.setText(mAuth.getCurrentUser().getEmail());

        //bottone che permette di andare nel fragment di creazione del CustomDrink
        FloatingActionButton mAddDrinks = view.findViewById(R.id.floating_action_button_profile_add_drink);
        mAddDrinks.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.action_profileFragment_to_customDrinkFragment));

    }

    //setta l'immagine dell'utente all'interno del fragment
    private void setUserImage() {
        //prende il riferimento nello storage dello userImage
        StorageReference fileRef = mStorageReference
                .child("UserImage/" + Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + "/profile.jpg");
        //carica l'immagine nella circle imageView
        fileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(mUserImage));
    }

}