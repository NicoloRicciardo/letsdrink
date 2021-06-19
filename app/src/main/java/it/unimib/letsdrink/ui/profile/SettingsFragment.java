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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Objects;
import it.unimib.letsdrink.R;

public class SettingsFragment extends Fragment {

    private static final String TAG = "TempSettingsFragment";
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DocumentReference documentReference;
    private StorageReference storageReference;

    public SettingsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("Impostazioni");

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

        Button changeImageProfile = view.findViewById(R.id.button_settings_change_image);
        changeImageProfile.setOnClickListener(v -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 1000);
        });

        Button btnModUsername = view.findViewById(R.id.button_settings_change_username);
        btnModUsername.setOnClickListener(v -> {
            EditText resetUsername = new EditText(getActivity());
            new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                    .setTitle("Modifica Username")
                    .setView(resetUsername)
                    .setPositiveButton("Conferma", (dialog, which) -> {
                        if (!resetUsername.getText().toString().isEmpty()) {
                            HashMap<String, Object> edited = new HashMap<>();
                            edited.put("userName", resetUsername.getText().toString());
                            documentReference.update(edited);
                        } else {
                            Toast.makeText(getContext(), "Error! Empty", Toast.LENGTH_SHORT).show();
                        }

                    })
                    .setNegativeButton("Esci", null)
                    .show();
        });

        Button btnModPassword = view.findViewById(R.id.button_settings_change_password);
        btnModPassword.setOnClickListener(v -> {
            EditText resetPassword = new EditText(getActivity());
            new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                    .setTitle("Modifica Password")
                    .setView(resetPassword)
                    .setPositiveButton("Conferma", (dialog, which) -> {
                        if (!resetPassword.getText().toString().isEmpty()) {
                            firebaseUser.updatePassword(resetPassword.getText().toString()).addOnSuccessListener(unused ->
                                    Toast.makeText(getContext(), "Password cambiata", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Password NON cambiata", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "eccezione: " + e);
                            });
                        } else {
                            Toast.makeText(getContext(), "Error! Empty", Toast.LENGTH_SHORT).show();
                        }

                    })
                    .setNegativeButton("Esci", null)
                    .show();
        });

        Button btnAboutUs = view.findViewById(R.id.button_settings_about);
        btnAboutUs.setOnClickListener(v -> new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                .setTitle("About us")
                .setMessage("L'applicazione mostra i cocktail")
                .setPositiveButton("OK", null)
                .show());

        Button btnPrivacy = view.findViewById(R.id.button_settings_privacy);
        btnPrivacy.setOnClickListener(v -> new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                .setTitle("Privacy Policy")
                .setMessage("Informazioni sulla raccolta e diffusione dei dati")
                .setPositiveButton("OK", null)
                .show());


        Button btnLogout = view.findViewById(R.id.button_settings_logout);
        btnLogout.setOnClickListener(v -> new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                .setTitle("Effettuare il logout")
                .setMessage("Sei sicuro di voler effettuare il logout?")
                .setPositiveButton("Conferma", (dialog, which) -> {
                    mAuth.signOut();
                    goOnLogin();
                })
                .setNegativeButton("Esci", null)
                .show());

        Button btnRemove = view.findViewById(R.id.button_settings_delete_account);
        btnRemove.setOnClickListener(v -> new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                .setTitle("Elimina Account")
                .setMessage("Sei sicuro di voler eliminare il tuo account?")
                .setPositiveButton("Conferma", (dialog, which) -> {
                    Objects.requireNonNull(mAuth.getCurrentUser()).delete();
                    goOnLogin();
                })
                .setNegativeButton("Esci", null)
                .show());
    }

    private void goOnLogin() {
        Navigation.findNavController(getView())
                .navigate(R.id.action_tempSettingsFragment_to_navigation_profile);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();

                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // upload image to firebase storage
        StorageReference fileRef = storageReference.child("UserImage/" + mAuth.getCurrentUser().getUid() + "/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                Toast.makeText(getContext(), "Immagine creata", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Immagine NON creata", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "errore" + e);
        });
    }
}