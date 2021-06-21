package it.unimib.letsdrink.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Objects;
import it.unimib.letsdrink.R;

//fragment delle impostazioni
public class SettingsFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DocumentReference documentReference;
    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(R.string.title_setting);

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        documentReference = mFirestore.collection("Utenti").document(firebaseUser.getUid());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //click sul bottone per cambiare immagine
        Button changeImageProfile = view.findViewById(R.id.button_settings_change_image);
        changeImageProfile.setOnClickListener(v -> {
            //intent per aprire la galleria
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 1000);
        });

        //click sul bottone per modificare l'username
        Button btnModUsername = view.findViewById(R.id.button_settings_change_username);
        btnModUsername.setOnClickListener(v -> {
            EditText resetUsername = new EditText(getActivity());
            //mostra alert dialog
            new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                    .setTitle(R.string.title_change_username)
                    .setView(resetUsername)
                    .setPositiveButton(R.string.button_confirm, (dialog, which) -> {
                        //se l'edittext non é vuota
                        if (!resetUsername.getText().toString().isEmpty()) {
                            //cambia lo username su firestore
                            HashMap<String, Object> edited = new HashMap<>();
                            edited.put("userName", resetUsername.getText().toString());
                            documentReference.update(edited);
                            //altrimenti mostra un messaggio di errore
                        } else {
                            Toast.makeText(getContext(), R.string.toast_error_empty, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(R.string.button_exit, null)
                    .show();
        });

        //click sul bottone per modificare la password
        Button btnModPassword = view.findViewById(R.id.button_settings_change_password);
        btnModPassword.setOnClickListener(v -> {
            EditText resetPassword = new EditText(getActivity());
            //mostra alert dialog
            new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                    .setTitle(R.string.title_change_password)
                    .setView(resetPassword)
                    .setPositiveButton(R.string.button_confirm, (dialog, which) -> {
                        //se l'edittext non é vuota
                        if (!resetPassword.getText().toString().isEmpty()) {
                            //cambia la password su firebaseAuth
                            firebaseUser.updatePassword(resetPassword.getText().toString()).addOnSuccessListener(unused ->
                                    Toast.makeText(getContext(), R.string.toast_changed_password, Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> Toast.makeText(getContext(), R.string.toast_NOT_changed_password, Toast.LENGTH_SHORT).show());
                            //altrimenti mostra un messaggio di errore
                        } else {
                            Toast.makeText(getContext(), R.string.toast_error_empty, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(R.string.button_exit, null)
                    .show();
        });

        //click sul bottone about
        Button btnAboutUs = view.findViewById(R.id.button_settings_about);
        btnAboutUs.setOnClickListener(v -> new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                //mostra una dialog con la descrizione dell'app
                .setTitle(R.string.title_about)
                .setMessage(R.string.message_about)
                .setPositiveButton(R.string.button_ok, null)
                .show());

        //click sul bottone privacy
        Button btnPrivacy = view.findViewById(R.id.button_settings_privacy);
        btnPrivacy.setOnClickListener(v -> new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                //mostra una dialog con le policy della privacy
                .setTitle(R.string.title_privacy)
                .setMessage(R.string.message_privacy)
                .setPositiveButton(R.string.button_ok, null)
                .show());

        //click sul bottone logout
        Button btnLogout = view.findViewById(R.id.button_settings_logout);
        //richiede la conferma di logout
        btnLogout.setOnClickListener(v -> new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                .setTitle(R.string.title_logout)
                .setMessage(R.string.message_logout)
                .setPositiveButton(R.string.button_confirm, (dialog, which) -> {
                    //effettua il logout e riporta al login
                    mAuth.signOut();
                    goOnLogin();
                })
                .setNegativeButton(R.string.button_exit, null)
                .show());

        //bottone per rimuovere un account
        Button btnRemove = view.findViewById(R.id.button_settings_delete_account);
        //richiede la conferma dell'eliminazione
        btnRemove.setOnClickListener(v -> new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                .setTitle(R.string.title_delete_account)
                .setMessage(R.string.message_delete_account)
                .setPositiveButton(R.string.button_confirm, (dialog, which) -> {
                    //elimina i dati da FirebaseAuth e da Firestore
                    CollectionReference collezione = FirebaseFirestore.getInstance().collection("Utenti");
                    collezione.document(firebaseUser.getUid()).delete();
                    Objects.requireNonNull(mAuth.getCurrentUser()).delete();
                    goOnHome();
                })
                .setNegativeButton(R.string.button_exit, null)
                .show());
    }

    //metodo che manda al login (quando fai logout)
    private void goOnLogin() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_settingsFragment_to_navigation_profile);
    }

    //metodo che manda alla home (quando elimini l'account)
    private void goOnHome() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_settingsFragment_to_navigation_drinks);
    }

    //metodo che gestisce il cambio immagine
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Uri imageUri = data.getData();

                uploadImageToFirebase(imageUri);
            }
        }
    }

    //metodo che inserisce l'immagine dello user su firebase
    private void uploadImageToFirebase(Uri imageUri) {
        //carica l'immagine su firebase storage
        StorageReference fileRef = storageReference.child("UserImage/" + Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                Toast.makeText(getContext(), R.string.toast_changed_user_image, Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getContext(), R.string.toast_NOT_changed_user_image, Toast.LENGTH_SHORT).show());
    }
}