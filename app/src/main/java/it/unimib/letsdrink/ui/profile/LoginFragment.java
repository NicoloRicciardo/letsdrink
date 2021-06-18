package it.unimib.letsdrink.ui.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shobhitpuri.custombuttons.GoogleSignInButton;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import it.unimib.letsdrink.R;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    GoogleSignInButton googleSignInButton;

    private TextInputEditText mEmail;
    private TextInputEditText mPassword;

    private TextInputLayout mLayoutEmail;
    private TextInputLayout mLayoutPassword;

    private CheckBox mRememberMe;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    public LoginFragment() {
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

        createRequest();

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    private void createRequest() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity().getApplicationContext(), gso);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmail = view.findViewById(R.id.text_field_login_email);
        mPassword = view.findViewById(R.id.text_field_login_password);

        mLayoutEmail = view.findViewById(R.id.text_layout_login_email);
        mLayoutPassword = view.findViewById(R.id.text_layout_login_password);

        mRememberMe = view.findViewById(R.id.checkbox_login_remember);
        mPrefs = this.getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        SharedPreferences sp = this.getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if(sp.contains("pref_email")) {
            String u = sp.getString("pref_email", "not found");
            mEmail.setText(u.toString());
        }
        if(sp.contains("pref_password")) {
            String a = sp.getString("pref_password", "not found");
            mPassword.setText(a.toString());
        }
        if(sp.contains("pref_check")) {
            boolean b = sp.getBoolean("pref_check", false);
            mRememberMe.setChecked(b);
        }

        Button buttonForgotPassword = view.findViewById(R.id.button_login_forgot_password);
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetEmail = new EditText(getActivity());

                new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                        .setTitle("Reset Password?")
                        .setMessage("Inserisci la tua Email per ricevere il link di reset.")
                        .setView(resetEmail)
                        .setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //extract the email and send reset link

                                if(!resetEmail.getText().toString().isEmpty()) {
                                    mAuth.sendPasswordResetEmail(resetEmail.getText().toString().trim())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(getContext(), "Reset link sent to your Email.", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Error! Reset link not sent.", Toast.LENGTH_SHORT).show();
                                                    Log.d(TAG, "errore" + e);
                                                }
                                            });
                                }else{
                                    Toast.makeText(getContext(), "Error! Empty", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Esci", null)
                        .show();
            }
        });

        Button buttonLogin = view.findViewById(R.id.button_login_access);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlLoginFields()) {
                    if(mRememberMe.isChecked()) {
                        boolean isChecked = mRememberMe.isChecked();
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString("pref_email", mEmail.getText().toString());
                        editor.putString("pref_password", mPassword.getText().toString());
                        editor.putBoolean("pref_check", isChecked);
                        editor.apply();
                    } else {
                        mPrefs.edit().clear().apply();
                    }
                   signInNormal();
                }
                //mEmail.getText().clear();
                //mPassword.getText().clear();
            }
        });

        googleSignInButton = view.findViewById(R.id.button_login_google);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button_login_google) {
                    Log.d(TAG, "Cliccato Google");
                    signIn();
                }
            }
        });

        Button buttonGoToRegistration = view.findViewById(R.id.button_login_sign_up);
        buttonGoToRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView())
                        .navigate(R.id.action_navigation_profile_to_registrationFragment);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            goOnProfile();
        }
    }

    private boolean controlLoginFields() {
        boolean email;
        boolean password;

        if (mEmail != null && !(Objects.requireNonNull(mEmail.getText())
                .toString().trim().isEmpty())) {
            email = true;
            mLayoutEmail.setError(null);
        } else {
            email = false;
            mLayoutEmail.setError(getText(R.string.error_email));
        }

        if (mPassword != null && !(Objects.requireNonNull(mPassword.getText())
                .toString().trim().isEmpty())) {
            password = true;
            mLayoutPassword.setError(null);
        } else {
            password = false;
            mLayoutPassword.setError(getText(R.string.error_password));
        }

        return email && password;
    }

    private void signInNormal() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(Objects.requireNonNull(mEmail
                .getText()).toString().trim(), Objects.requireNonNull(mPassword.getText())
                .toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getContext(), "Accesso effettuato.", Toast.LENGTH_SHORT).show();
                            Navigation.findNavController(getView()).navigate(R.id.action_navigation_profile_to_profileFragment);
                            Log.d(TAG, "loggedUserWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "loggedUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "entrato RC_SIGN_IN");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                Log.d(TAG, "entrato nel try");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
                Log.d(TAG, "fine try");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Log.d(TAG,"error " + e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) requireContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            storeData();
                            FirebaseUser user = mAuth.getCurrentUser();
                            goOnProfile();
                            /*FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();*/
                        } else {
                            // If sign in fails, display a message to the user.
                            Snackbar.make(requireView(), "Autenticazione fallita", Snackbar.LENGTH_LONG)
                                    .show();
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private void storeData() {
        /*user.setUserID(mAuth.getCurrentUser().getUid());*/
        DocumentReference documentReference = mFirestore.collection("Utenti").document(mAuth.getCurrentUser().getUid());

        String email = mAuth.getCurrentUser().getEmail();

        Map<String, Object> userDB = new HashMap<>();
        userDB.put("userName", email.substring(0,email.indexOf("@")));
        userDB.put("age", "18");
        userDB.put("email", email);
        //TODO aggiungere arraylist dei custom drinks (vuoto)

        documentReference.set(userDB).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "profilo creato su firestore");
            }
        });
    }

    private void goOnProfile(){
        Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_profile_to_profileFragment);
    }

}