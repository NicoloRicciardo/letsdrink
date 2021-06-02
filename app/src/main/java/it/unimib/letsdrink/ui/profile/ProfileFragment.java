package it.unimib.letsdrink.ui.profile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import it.unimib.letsdrink.R;

public class ProfileFragment extends Fragment {

    //private ProfileViewModel profileViewModel;
    private TextView mUserNameCustom;
    private TextView mEmailCustom;
    private TextView mPlaceholder;
    private FloatingActionButton mAddDrinks;

    User user = new User();
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        ActionBar actionBar= ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("Profilo");
        actionBar.setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings_item:
                /*this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);*/
                Navigation.findNavController(getView())
                        .navigate(R.id.action_profileFragment_to_settingsFragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mUserNameCustom = view.findViewById(R.id.text_profile_user_name);

        user.setUserID(mAuth.getUid());
        DocumentReference documentReference = mFirestore.collection("Utenti").document(user.getUserID());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    mUserNameCustom.setText(documentSnapshot.getString("userName"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("ProfileFragment", "OnFailure mUserName");
            }
        });

        mEmailCustom = view.findViewById(R.id.text_profile_email);
        mEmailCustom.setText(mAuth.getCurrentUser().getEmail());

        mPlaceholder = view.findViewById(R.id.text_profile_not_logged_in);
        mPlaceholder.setText(R.string.value_no_custom_drinks);

        mAddDrinks = view.findViewById(R.id.floating_action_button_profile_add_drink);
        mAddDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_profileFragment_to_customDrinkFragment);
            }
        });

        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> edited = new HashMap<>();
                edited.put("userName", "gianpeppo");
                /*edited.put("email", "richmnico@gmail.com");
                edited.put("age", "23");*/
                /*documentReference.set(edited);*/
                documentReference.update(edited);
            }
        });

    }

}