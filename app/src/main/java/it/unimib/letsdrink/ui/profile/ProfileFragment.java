package it.unimib.letsdrink.ui.profile;

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
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

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
        /*profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);*/
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        //final TextView textView = root.findViewById(R.id.text_profile);
        /*profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        //setHasOptionsMenu(true);
        return root;
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

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}