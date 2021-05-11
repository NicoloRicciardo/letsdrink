package it.unimib.letsdrink.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import it.unimib.letsdrink.R;


public class UserAccessFragment extends Fragment {

    Button btn_registrazione;
    Button btn_accedi;

    public UserAccessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_registrazione = view.findViewById(R.id.button_registrazione);
        btn_accedi = view.findViewById(R.id.button_accedi);

        btn_registrazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr = new RegistrationFragment();
                FragmentChangeListener fc = (FragmentChangeListener)getActivity();
                fc.replaceFragment(fr);
            }
        });

        btn_accedi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fr = new LoginFragment();
                FragmentChangeListener fc = (FragmentChangeListener)getActivity();
                fc.replaceFragment(fr);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_access, container, false);
    }
}