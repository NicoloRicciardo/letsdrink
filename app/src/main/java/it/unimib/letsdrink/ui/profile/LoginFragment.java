package it.unimib.letsdrink.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import it.unimib.letsdrink.R;


public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    private TextInputEditText mEmail;
    private TextInputEditText mPassword;

    private TextInputLayout mLayoutEmail;
    private TextInputLayout mLayoutPassword;

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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmail = view.findViewById(R.id.text_field_login_email);
        mPassword = view.findViewById(R.id.text_field_login_password);

        mLayoutEmail = view.findViewById(R.id.text_layout_login_email);
        mLayoutPassword = view.findViewById(R.id.text_layout_login_password);

        Button mButtonLogin = view.findViewById(R.id.button_login_access);
        Button mButtonGoToRegistration = view.findViewById(R.id.button_login_sign_up);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (controlLoginFields()) {
                   signInNormal();
                }
            }
        });

        mButtonGoToRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr = new RegistrationFragment();
                FragmentChangeListener fc = (FragmentChangeListener)getActivity();
                assert fc != null;
                fc.replaceFragment(fr);
            }
        });
    }

    private boolean controlLoginFields() {
        boolean email;
        boolean password;

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
            mLayoutPassword.setError("Password inserita non valida.");
        }

        return email && password;
    }

    private void signInNormal() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(Objects.requireNonNull(mEmail.getText()).toString().trim(), Objects.requireNonNull(mPassword.getText()).toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "loggedUserWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "loggedUserWithEmail:failure", task.getException());
                        }
                    }
                });
    }
}