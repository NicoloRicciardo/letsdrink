package it.unimib.letsdrink.ui.profile;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import it.unimib.letsdrink.R;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Navigation.findNavController(getView())
                    .navigate(R.id.action_settingsFragment_to_profileFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ActionBar actionBar= ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("Impostazioni");
        // actionBar.setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        /*ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);*/

        FragmentManager fm = requireActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.layout_settings, new Settings(container)).commit();

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }



    public static class Settings extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

        private ViewGroup vg;
        private FirebaseAuth mAuth = FirebaseAuth.getInstance();
        private FirebaseFirestore mFirestore= FirebaseFirestore.getInstance();
        User user = new User();
        DocumentReference documentReference;

        public Settings(ViewGroup vg) {
            this.vg = vg;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            user.setUserID(mAuth.getCurrentUser().getUid());
            documentReference = mFirestore.collection("Utenti").document(user.getUserID());

            addPreferencesFromResource(R.xml.settings);

            final Preference image = findPreference("imageKey");
            assert image != null;
            image.getIcon().setTint(Color.WHITE);

            final Preference name = findPreference("nameKey");
            assert name != null;
            name.getIcon().setTint(Color.WHITE);
            /*EditText resetName = new EditText(getActivity());
            name.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new MaterialAlertDialogBuilder(requireActivity(), R.style.DialogTheme)
                            .setTitle("Cambia username")
                            .setView(resetName)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.d(TAG, "name change entrati");
                                    HashMap<String, Object> edited = new HashMap<>();
                                    edited.put("userName", resetName.getText().toString());
                                    documentReference.update(edited);
                                }
                            })
                            .setNegativeButton("Annulla", null)
                            .setBackground(new ColorDrawable(Color.TRANSPARENT))
                            .show();
                    return true;
                }
            });*/

            final Preference password = findPreference("passwordKey");
            assert password != null;
            password.getIcon().setTint(Color.WHITE);

            final Preference aboutUs = findPreference("aboutKey");
            assert aboutUs != null;
            aboutUs.getIcon().setTint(Color.WHITE);
            aboutUs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new MaterialAlertDialogBuilder(requireActivity(), R.style.DialogTheme)
                            .setTitle("About us")
                            .setMessage("L'applicazione mostra i cocktail")
                            .setPositiveButton("OK", null)
                            .setBackground(new ColorDrawable(Color.TRANSPARENT))
                            .show();
                    return true;
                }
            });

            final Preference privacy = findPreference("privacyKey");
            assert privacy != null;
            privacy.getIcon().setTint(Color.WHITE);
            privacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new MaterialAlertDialogBuilder(requireActivity(), R.style.DialogTheme)
                            .setTitle("Privacy Policy")
                            .setMessage("Informazioni sulla raccolta e diffusione dei dati")
                            .setPositiveButton("OK", null)
                            .setBackground(new ColorDrawable(Color.TRANSPARENT))
                            .show();
                    return true;
                }
            });

            /* */
            final Preference version = findPreference("versionKey");
            assert version != null;
            version.getIcon().setTint(Color.WHITE);
            /* */

            final Preference logout = findPreference("logoutKey");
            assert logout != null;
            logout.getIcon().setTint(Color.WHITE);
            logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    FirebaseAuth.getInstance().signOut();
                    goOnLogin();
                    return true;
                }
            });

            final Preference remove = findPreference("removeKey");
            assert remove != null;
            remove.getIcon().setTint(Color.WHITE);

        }

        private void goOnLogin() {
            Navigation.findNavController(getView())
                    .navigate(R.id.action_settingsFragment_to_navigation_profile);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            switch (key) {
                case "nameKey":
                    String name = sharedPreferences.getString("nameKey", "");
                    /*Snackbar.make(requireView(), "Nome:  " + name, Snackbar.LENGTH_SHORT)
                            .show();*/

                    HashMap<String, Object> edited = new HashMap<>();
                    edited.put("userName", name);
                    documentReference.update(edited);
                    break;
                case "passwordKey":
                    String password = sharedPreferences.getString("passwordKey", "");

                    user.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    break;
            }
        }

        @Override
        public void onStart() {
            super.onStart();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onStop() {
            super.onStop();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }

    }
}