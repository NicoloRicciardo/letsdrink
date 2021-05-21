package it.unimib.letsdrink.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.airbnb.lottie.L;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
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

public class RegistrationFragment extends Fragment {

    // private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    GoogleSignInButton googleSignInButton;

    private static final String TAG = "RegistrationFragment";

    private TextInputEditText mUserName;
    private TextInputEditText mAge;
    private TextInputEditText mEmail;
    private TextInputEditText mPassword;
    private TextInputEditText mConfirmPassword;

    private TextInputLayout mLayoutUserName;
    private TextInputLayout mLayoutAge;
    private TextInputLayout mLayoutEmail;
    private TextInputLayout mLayoutPassword;
    private TextInputLayout mLayoutConfirmPassword;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private View mRegistrationFragment;

    User user = new User();

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        createRequest();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
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

        mUserName = view.findViewById(R.id.text_field_registration_user_name);
        mAge = view.findViewById(R.id.text_field_registration_age);
        mEmail = view.findViewById(R.id.text_field_registration_email);
        mPassword = view.findViewById(R.id.text_field_registration_password);
        mConfirmPassword = view.findViewById(R.id.text_field_registration_confirm_password);

        mLayoutUserName = view.findViewById(R.id.text_layout_registration_user_name);
        mLayoutAge = view.findViewById(R.id.text_layout_registration_age);
        mLayoutEmail = view.findViewById(R.id.text_layout_registration_email);
        mLayoutPassword = view.findViewById(R.id.text_layout_registration_password);
        mLayoutConfirmPassword = view.findViewById(R.id.text_layout_registration_confirm_password);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        Button mButtonRegistration = view.findViewById(R.id.button_registration);
        Button mButtonGoToLogin = view.findViewById(R.id.button_registration_sign_in);
        googleSignInButton = view.findViewById(R.id.button_registration_google);

        mRegistrationFragment = view.findViewById(R.id.fragment_registration);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button_registration_google) {
                    Log.d(TAG, "Cliccato Google");
                    signIn();
                }
            }
        });

        mButtonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUser_name(Objects.requireNonNull(mUserName.getText()).toString().trim());
                user.setAge(Objects.requireNonNull(mAge.getText()).toString().trim());
                user.setEmail(Objects.requireNonNull(mEmail.getText()).toString().trim());

                if(controlRegistrationFields()) {
                    signUpNormal();
                }
            }
        });

        mButtonGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Fragment fr = new LoginFragment();
                FragmentChangeListener fc = (FragmentChangeListener)getActivity();
                assert fc != null;
                fc.replaceFragment(fr);*/

                /*Fragment fragment = new LoginFragment();
                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_registration, fragment);
                ft.commit();*/

                /*FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.navigation_profile, new LoginFragment()).addToBackStack("accreg").commit();*/

                /*LoginFragment loginFragment = new LoginFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, loginFragment);
                fragmentTransaction.commit();*/

                Navigation.findNavController(getView()).navigate(R.id.action_navigation_profile_to_loginFragment);
            }
        });
    }

    private void storeData() {
        user.setUserID(mAuth.getCurrentUser().getUid());
        DocumentReference documentReference = mFirestore.collection("Utenti").document(user.getUserID());

        Map<String, Object> userDB = new HashMap<>();
        userDB.put("userName", user.getUser_name());
        userDB.put("age", user.getAge());
        userDB.put("email", user.getEmail());

        documentReference.set(userDB).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "profilo creato su firestore");
            }
        });
    }

    // controlla che le text field non sia vuota
    private boolean controlRegistrationFields() {
        boolean userName;
        boolean email;
        boolean age;
        boolean password;
        boolean confirmPassword = false;

        if (mUserName != null && !(Objects.requireNonNull(mUserName.getText()).toString().isEmpty())) {
            userName = true;
            mLayoutUserName.setError(null);
        } else {
            userName = false;
            assert mUserName != null;
            mLayoutUserName.setError("Nome Utente inserito non valido.");
        }

        if (mAge != null && !(Objects.requireNonNull(mAge.getText()).toString().trim().isEmpty())) {
            age = true;
            mLayoutAge.setError(null);
        } else {
            age = false;
            assert mAge != null;
            mLayoutAge.setError("Età inserita non valida.");
        }

        if (mEmail != null && !(Objects.requireNonNull(mEmail.getText()).toString().trim().isEmpty())) {
            email = true;
            mLayoutEmail.setError(null);
        } else {
            email = false;
            mLayoutEmail.setError("Email inserita non valida");
        }

        if (mPassword != null && !(Objects.requireNonNull(mPassword.getText()).toString().trim().isEmpty())) {
            password = true;
            mLayoutPassword.setError(null);
        } else {
            password = false;
            mLayoutPassword.setError("La Password deve essere di almeno 6 caratteri.");
        }

        if (mConfirmPassword != null && !(Objects.requireNonNull(mConfirmPassword.getText()).toString().trim().isEmpty())) {
            if(mConfirmPassword.getText().toString().trim().equals(Objects.requireNonNull(mPassword.getText()).toString().trim())) {
                confirmPassword = true;
                mLayoutConfirmPassword.setError(null);
            } //TODO settare errore
        } else {
            mLayoutConfirmPassword.setError("Password inserita non valida.");
        }

        return userName && email && age && password && confirmPassword;
    }

    private void signUpNormal() {
        mAuth.createUserWithEmailAndPassword(Objects
                .requireNonNull(mEmail.getText()).toString().trim(),
                Objects.requireNonNull(mPassword.getText()).toString().trim())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            storeData();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
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

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) requireContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            goOnProfile();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
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
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void goOnProfile(){
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_registration, new ProfileFragment()).commit();
    }
}