package it.unimib.letsdrink.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shobhitpuri.custombuttons.GoogleSignInButton;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import it.unimib.letsdrink.R;

//fragment del login
public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    GoogleSignInButton googleSignInButton;
    private TextInputEditText mEmail, mPassword;
    private TextInputLayout mLayoutEmail, mLayoutPassword;
    private CheckBox mRememberMe;
    private SharedPreferences mPrefs;
    private static final String PREFS_NAME = "PrefsFile";
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //richiama il metodo per gestire il login con google
        createRequest();

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    //configura Google Sign In
    private void createRequest() {
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
        mPrefs = this.requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        //permette di ricordarsi i dati inseriti dall'utente
        SharedPreferences sp = this.requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if (sp.contains("pref_email")) {
            String u = sp.getString("pref_email", "not found");
            mEmail.setText(u);
        }
        if (sp.contains("pref_password")) {
            String a = sp.getString("pref_password", "not found");
            mPassword.setText(a);
        }
        if (sp.contains("pref_check")) {
            boolean b = sp.getBoolean("pref_check", false);
            mRememberMe.setChecked(b);
        }

        //gestione bottone "password dimenticata"
        Button buttonForgotPassword = view.findViewById(R.id.button_login_forgot_password);
        buttonForgotPassword.setOnClickListener(v -> {
            EditText resetEmail = new EditText(getActivity());

            //viene mostrata una alert dialog
            new MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme)
                    .setTitle("Reset Password?")
                    .setMessage("Inserisci la tua Email per ricevere il link di reset.")
                    .setView(resetEmail)
                    .setPositiveButton("Conferma", (dialog, which) -> {
                        //se il campo edittext non é vuoto
                        if (!resetEmail.getText().toString().isEmpty()) {
                            //estrae l'email dalla edittext e manda il link di reset (se l'email esiste su firebase)
                            mAuth.sendPasswordResetEmail(resetEmail.getText().toString().trim())
                                    //reset link inviato
                                    .addOnSuccessListener(unused -> Toast.makeText(getContext(), "Reset link sent to your Email.", Toast.LENGTH_SHORT).show())
                                    //reset link non inviato
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getContext(), "Error! Reset link not sent.", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "errore" + e);
                                    });
                            //se l'email é vuota mostra un messaggio di errore
                        } else {
                            Toast.makeText(getContext(), "Error! Empty", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Esci", null)
                    .show();
        });

        Button buttonLogin = view.findViewById(R.id.button_login_access);
        buttonLogin.setOnClickListener(v -> {
            //controlla se i campi inseriti sono corretti per fare il login
            if (controlLoginFields()) {
                //se la checkbox "ricordami" é checkata
                if (mRememberMe.isChecked()) {
                    boolean isChecked = mRememberMe.isChecked();
                    //crea un editor sharedPref per salvare al suo interno i dati dell'utente
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("pref_email", Objects.requireNonNull(mEmail.getText()).toString());
                    editor.putString("pref_password", Objects.requireNonNull(mPassword.getText()).toString());
                    editor.putBoolean("pref_check", isChecked);
                    editor.apply();
                    //se la checkbox non é premuta, non salva i dati dell'utente
                } else {
                    mPrefs.edit().clear().apply();
                }
                //effettua il login con email e password
                signInNormal();
            }
        });

        //click del pulsante "continua con google"
        googleSignInButton = view.findViewById(R.id.button_login_google);
        googleSignInButton.setOnClickListener(v -> {
            if (v.getId() == R.id.button_login_google) {
                Log.d(TAG, "Cliccato Google");
                signIn();
            }
        });

        //bottone che cambia fragment, manda a quello di registrazione
        Button buttonGoToRegistration = view.findViewById(R.id.button_login_sign_up);
        buttonGoToRegistration.setOnClickListener(v -> Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_profile_to_registrationFragment));

    }

    //metodo che viene chiamato all'inizio del fragment
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //controlla che l'utente sia giá loggato (non-null)
        if (currentUser != null) {
            //aggiorna la UI, manda sul profilo
            goOnProfile();
        }
    }

    //controllo dei campi del login
    private boolean controlLoginFields() {
        boolean email;
        boolean password;

        //email inserita valida
        if (mEmail != null && !(Objects.requireNonNull(mEmail.getText())
                .toString().trim().isEmpty())) {
            email = true;
            mLayoutEmail.setError(null);
            //email inserita NON valida, setta un errore
        } else {
            email = false;
            mLayoutEmail.setError(getText(R.string.error_email));
        }

        //password inserita valida
        if (mPassword != null && !(Objects.requireNonNull(mPassword.getText())
                .toString().trim().isEmpty())) {
            password = true;
            mLayoutPassword.setError(null);
            //password inserita NON valida, setta un errore
        } else {
            password = false;
            mLayoutPassword.setError(getText(R.string.error_password));
        }

        return email && password;
    }

    //si occupa di fare il login con email e password
    private void signInNormal() {
        //prende e controlla l'email e la password inserite dall'utente
        FirebaseAuth.getInstance().signInWithEmailAndPassword(Objects.requireNonNull(mEmail
                .getText()).toString().trim(), Objects.requireNonNull(mPassword.getText())
                .toString().trim()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //il login ha successo
                Toast.makeText(getContext(), "Accesso effettuato.", Toast.LENGTH_SHORT).show();
                //schermata cambiata sul profilo
                Navigation.findNavController(requireView()).navigate(R.id.action_navigation_profile_to_profileFragment);
            } else {
                //il login non ha successe, viene mostrato un messaggio di errore
                Toast.makeText(getContext(), "Accesso NON effettuato.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //creazione intent per login con google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //gestito intent di login con google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // risultato ritornato dal lancio dell'intent di GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //google sign in ha avuto successo, autenticazione con Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account.getIdToken());
                Toast.makeText(getContext(), "Accesso con Google effettuato.", Toast.LENGTH_SHORT).show();
            } catch (ApiException e) {
                //google sign in fallito
                Toast.makeText(getContext(), "Accesso con Google fallito.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //metodo per la gestione dell'account di google su firebase
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) requireContext(), task -> {
                    if (task.isSuccessful()) {
                        //il sign in ha successo, salva i dati dell'utente su Firestore
                        storeData();
                        //manda alla schermata del profilo
                        goOnProfile();
                    } else {
                        //sign in fallisce, mostra un messaggio
                        Toast.makeText(getContext(), "Autenticazione fallita", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //metodo per salvare i dati su Firestore (per chi fa accesso con google)
    private void storeData() {

        DocumentReference documentReference = mFirestore.collection("Utenti").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());

        String email = mAuth.getCurrentUser().getEmail();

        //crea un oggetto HashMap
        Map<String, Object> userDB = new HashMap<>();
        assert email != null;
        //viene settato come username l'email fino alla "@"
        userDB.put("userName", email.substring(0, email.indexOf("@")));
        userDB.put("age", "18");
        userDB.put("email", email);

        documentReference.set(userDB).addOnSuccessListener(aVoid -> Log.d(TAG, "profilo creato su firestore"));
    }

    //metodo che manda sul profilo
    private void goOnProfile() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_profile_to_profileFragment);
    }

}