package it.unimib.letsdrink.ui.profile;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import it.unimib.letsdrink.R;

public class TempSettingsFragment extends Fragment {

    private static final String TAG = "TempSettingsFragment";

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseUser firebaseUser;
    private DocumentReference documentReference;

    public TempSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ActionBar actionBar= ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("Impostazioni");

        return inflater.inflate(R.layout.fragment_temp_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String userName = "", age, email, id;

        mFirestore= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        documentReference = mFirestore.collection("Utenti").document(firebaseUser.getUid());

        Button btnModUsername = view.findViewById(R.id.button_settings_change_username);
        btnModUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetUsername = new EditText(getActivity());
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Modifica Username")
                        .setView(resetUsername)
                        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!resetUsername.getText().toString().isEmpty()) {
                                    HashMap<String, Object> edited = new HashMap<>();
                                    edited.put("userName", resetUsername.getText().toString());
                                    documentReference.update(edited);
                                }else{
                                    Toast.makeText(getContext(), "Error! Empty", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("Esci", null)
                        .show();
            }
        });

        Button btnModPassword = view.findViewById(R.id.button_settings_change_password);
        btnModPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetPassword = new EditText(getActivity());
                new MaterialAlertDialogBuilder(requireActivity())
                        .setTitle("Modifica Username")
                        .setView(resetPassword)
                        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!resetPassword.getText().toString().isEmpty()) {
                                    firebaseUser.updatePassword(resetPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getContext(), "Password cambiata", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Toast.makeText(getContext(), "Password NON cambiata", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, "eccezione: " + e);
                                        }
                                    });
                                }else{
                                    Toast.makeText(getContext(), "Error! Empty", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("Esci", null)
                        .show();

                Button btnAboutUs = view.findViewById(R.id.button_settings_about);
                btnAboutUs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MaterialAlertDialogBuilder(requireActivity(), R.style.DialogTheme)
                                .setTitle("About us")
                                .setMessage("L'applicazione mostra i cocktail")
                                .setPositiveButton("OK", null)
                                .setBackground(new ColorDrawable(Color.TRANSPARENT))
                                .show();
                    }
                });

                Button btnPrivacy = view.findViewById(R.id.button_settings_privacy);
                btnPrivacy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MaterialAlertDialogBuilder(requireActivity(), R.style.DialogTheme)
                                .setTitle("Privacy Policy")
                                .setMessage("Informazioni sulla raccolta e diffusione dei dati")
                                .setPositiveButton("OK", null)
                                .setBackground(new ColorDrawable(Color.TRANSPARENT))
                                .show();
                    }
                });


                Button btnLogout = view.findViewById(R.id.button_settings_logout);
                btnLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAuth.signOut();
                        goOnLogin();
                    }
                });

            }
        });

    }

    private void goOnLogin() {
        Navigation.findNavController(getView())
                .navigate(R.id.action_tempSettingsFragment_to_navigation_profile);
    }
}