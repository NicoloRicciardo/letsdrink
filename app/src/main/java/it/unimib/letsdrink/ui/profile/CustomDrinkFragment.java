package it.unimib.letsdrink.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;

public class CustomDrinkFragment extends Fragment {

    private static final String TAG = "CustomDrinkFragment";

    private LinearLayout mLayoutIngredientsList;
    private ImageView mAddIngredient;

    private EditText mNameCustomDrink;
    private EditText mMethodCustomDrink;

    private ImageView mSetDrinkPhoto;

    private ArrayList<String> mIngredientsCustomDrink = new ArrayList<>();

    private FloatingActionButton mCreateCustomDrink;

    private Cocktail mCustomDrink = new Cocktail();

    private FirebaseFirestore db;
    private CollectionReference collezione;
    FirebaseUser user;


    public CustomDrinkFragment() {
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

        ActionBar actionBar= ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("Cocktail personalizzato");
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.fragment_custom_drink, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();
        collezione = db.collection("Utenti");

        mLayoutIngredientsList = view.findViewById(R.id.layout_ingredients_list);
        mAddIngredient = view.findViewById(R.id.image_add_ingredient);

        mNameCustomDrink = view.findViewById(R.id.edit_text_custom_drink_name);
        mMethodCustomDrink = view.findViewById(R.id.edit_text_custom_drink_method);

        mCustomDrink.setName(mNameCustomDrink.getText().toString().trim());
        mCustomDrink.setMethod(mMethodCustomDrink.getText().toString().trim());
        //TODO settare cambio immagine
        mCustomDrink.setImageUrl("");

        mSetDrinkPhoto = view.findViewById(R.id.image_custom_drink_camera);
        mSetDrinkPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "cliccata camera");
            }
        });

        mAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });

        if(controlIngredients()) {
            mCustomDrink.setIngredients(mIngredientsCustomDrink);
        }

        mCreateCustomDrink = view.findViewById(R.id.floating_action_button_custom_drink_add);
        mCreateCustomDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collezione.document(currentUser).collection("customDrink").add(mCustomDrink);
            }
        });
    }

    private boolean controlIngredients() {
        mIngredientsCustomDrink.clear();
        boolean result = true;

        for(int i = 0; i < mLayoutIngredientsList.getChildCount(); i++){

            View ingredientsListView = mLayoutIngredientsList.getChildAt(i);
            EditText ingredientInsert = (EditText)ingredientsListView.findViewById(R.id.edit_text_ingredient_list);

            if(!ingredientInsert.getText().toString().equals("")) {
                mIngredientsCustomDrink.add(ingredientInsert.getText().toString());
            } else {
                result = false;
                break;
            }
        }

        if(mIngredientsCustomDrink.size() == 0){
            result = false;
            Toast.makeText(getContext(), "Inserire ingredienti!", Toast.LENGTH_SHORT).show();
        } else if(!result){
            Toast.makeText(getContext(), "Inserire tutti i dati.", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    private void addView() {

        View ingredientsListView = getLayoutInflater().inflate(R.layout.ingredients_list, null,false);

        EditText ingredient = (EditText)ingredientsListView.findViewById(R.id.edit_text_ingredient_list);
        ImageView imageClose = (ImageView)ingredientsListView.findViewById(R.id.image_delete_ingredient);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(ingredientsListView);
            }
        });

        mLayoutIngredientsList.addView(ingredientsListView);

    }

    private void removeView(View view) {
        mLayoutIngredientsList.removeView(view);
    }
}