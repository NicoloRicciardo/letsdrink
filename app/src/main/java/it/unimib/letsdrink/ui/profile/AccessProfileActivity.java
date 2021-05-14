package it.unimib.letsdrink.ui.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import it.unimib.letsdrink.R;

public class AccessProfileActivity extends AppCompatActivity implements FragmentChangeListener{

    UserAccessFragment accessFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_profile);

        setAccessFragment();
        //onCloseActivity();
    }

    private void setAccessFragment() {
        accessFragment = new UserAccessFragment();
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.access_frame, accessFragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    /*private void onCloseActivity() {
        ImageButton btn_close = findViewById(R.id.button_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }*/

    @Override
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.access_frame, fragment);
        //fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }
}