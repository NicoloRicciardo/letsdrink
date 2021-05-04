package it.unimib.letsdrink.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import it.unimib.letsdrink.R;

public class AccessProfileActivity extends AppCompatActivity {

    private Button btn_registrazione;
    private com.google.android.material.button.MaterialButton btn_accedi;
    UserAccessFragment accessFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_profile);

        setAccessFragment();
        onCloseActivity();
        //setBtn_registrazione();
    }

    private void setAccessFragment() {
        accessFragment = new UserAccessFragment();
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.access_frame, accessFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void onCloseActivity() {
        ImageButton btn_close = findViewById(R.id.button_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setBtn_registrazione() {
        btn_registrazione = findViewById(R.id.button_registrazione);
        btn_registrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccessProfileActivity.this, RegistrationActivity.class));
                //uso le transizioni definite da me
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void setBtn_accedi() {

    }
}