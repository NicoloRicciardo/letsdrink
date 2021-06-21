package it.unimib.letsdrink.ui.profile;

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
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.User;

//fragment della registrazione
public class RegistrationFragment extends Fragment {

    private static final String TAG = "RegistrationFragment";
    private TextInputEditText mUserName, mAge, mEmail, mPassword, mConfirmPassword;
    private TextInputLayout mLayoutUserName, mLayoutAge, mLayoutEmail, mLayoutPassword, mLayoutConfirmPassword;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    User user = new User();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_registration, container, false);
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

        //al click del bottone di "registrati", setta i parametri dello user inseriti dall'utente nelle edittext
        mButtonRegistration.setOnClickListener(v -> {
            user.setUserName(Objects.requireNonNull(mUserName.getText()).toString().trim());
            user.setAge(Objects.requireNonNull(mAge.getText()).toString().trim());
            user.setEmail(Objects.requireNonNull(mEmail.getText()).toString().trim());

            //se i campi sono inseriti correttamente
            if (controlRegistrationFields()) {
                //permette di fare la registrazione
                signUpNormal();
            }
        });

        //manda alla schermata del profilo
        mButtonGoToLogin.setOnClickListener(v -> Navigation.findNavController(requireView()).navigate(R.id.action_registrationFragment_to_navigation_profile));
    }

    //metodo che permette di salvare i dati su firestore
    private void storeData() {
        user.setUserID(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        DocumentReference documentReference = mFirestore.collection("Utenti").document(user.getUserID());

        Map<String, Object> userDB = new HashMap<>();
        userDB.put("userName", user.getUserName());
        userDB.put("age", user.getAge());
        userDB.put("email", user.getEmail());

        documentReference.set(userDB).addOnSuccessListener(aVoid -> Log.d(TAG, "profilo creato su firestore"));
    }

    //controlla che le textview non siano vuote
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
            mLayoutUserName.setError(getText(R.string.error_user_name));
        }

        if (mAge != null && !(Objects.requireNonNull(mAge.getText()).toString().trim().isEmpty())) {
            age = true;
            mLayoutAge.setError(null);
        } else {
            age = false;
            assert mAge != null;
            mLayoutAge.setError(getText(R.string.error_age));
        }

        if (mEmail != null && !(Objects.requireNonNull(mEmail.getText()).toString().trim().isEmpty())
                && mEmail.getText().toString().contains("@")) {
            email = true;
            mLayoutEmail.setError(null);
        } else {
            email = false;
            mLayoutEmail.setError(getText(R.string.error_email));
        }

        if (mPassword != null && Objects.requireNonNull(mPassword.getText()).toString().trim().length() < 6) {
            password = false;
            mLayoutPassword.setError(getText(R.string.error_password_min));
        } else if (mPassword != null && !(Objects.requireNonNull(mPassword.getText()).toString().trim().isEmpty())) {
            password = true;
            mLayoutPassword.setError(null);
        } else {
            password = false;
            mLayoutPassword.setError(getText(R.string.error_password_min));
        }

        if (mConfirmPassword != null && !(Objects.requireNonNull(mConfirmPassword.getText()).toString().trim().isEmpty())) {
            if (mConfirmPassword.getText().toString().trim().equals(Objects.requireNonNull(mPassword.getText()).toString().trim())) {
                confirmPassword = true;
                mLayoutConfirmPassword.setError(null);
            }
        } else {
            mLayoutConfirmPassword.setError(getText(R.string.error_password));
        }

        return userName && email && age && password && confirmPassword;
    }

    //metodo che permette di fare la registrazione
    private void signUpNormal() {
        mAuth.createUserWithEmailAndPassword(Objects
                        .requireNonNull(mEmail.getText()).toString().trim(),
                Objects.requireNonNull(mPassword.getText()).toString().trim())
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // la registrazione ha successo
                        storeData();
                        goOnProfile();
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getContext(), "Email già in uso!", Toast.LENGTH_SHORT).show();
                        }
                        // la registrazione NON ha successo
                    }
                });
    }

    //metodo che manda sul profilo
    private void goOnProfile() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_registrationFragment_to_profileFragment);
    }
}