package it.unimib.letsdrink.ui.profile;

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

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import it.unimib.letsdrink.R;

public class SettingsFragment extends Fragment {

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

        public Settings(ViewGroup vg) {
            this.vg = vg;
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.settings);

            final Preference image = findPreference("imageKey");
            assert image != null;
            image.getIcon().setTint(Color.WHITE);

            final Preference name = findPreference("nameKey");
            assert name != null;
            name.getIcon().setTint(Color.WHITE);

            final Preference password = findPreference("passwordKey");
            assert password != null;
            password.getIcon().setTint(Color.WHITE);

            final Preference aboutUs = findPreference("aboutKey");
            assert aboutUs != null;
            aboutUs.getIcon().setTint(Color.WHITE);

            final Preference privacy = findPreference("privacyKey");
            assert privacy != null;
            privacy.getIcon().setTint(Color.WHITE);

            final Preference version = findPreference("versionKey");
            assert version != null;
            version.getIcon().setTint(Color.WHITE);

            final Preference logout = findPreference("logoutKey");
            assert logout != null;
            logout.getIcon().setTint(Color.WHITE);

            final Preference remove = findPreference("removeKey");
            assert remove != null;
            remove.getIcon().setTint(Color.WHITE);

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
        }
    }
}