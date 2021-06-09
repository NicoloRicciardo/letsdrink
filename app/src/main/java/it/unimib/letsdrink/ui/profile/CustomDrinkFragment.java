package it.unimib.letsdrink.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

        mCreateCustomDrink = view.findViewById(R.id.floating_action_button_custom_drink_add);
        mCreateCustomDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(controlParameters()) {
                    mCustomDrink.setIngredients(mIngredientsCustomDrink);
                    mCustomDrink.setName(mNameCustomDrink.getText().toString());
                    mCustomDrink.setMethod(mMethodCustomDrink.getText().toString());

                    collezione.document(currentUser).collection("customDrink").add(mCustomDrink);

                    Navigation.findNavController(getView())
                            .navigate(R.id.action_customDrinkFragment_to_profileFragment);
                } else {
                    Toast.makeText(getContext(), "Inserisci tutti i campi richiesti", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean controlParameters() {
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
        }

        if(mNameCustomDrink.getText().toString().equals("")) {
            result = false;
        }

        if(mMethodCustomDrink.getText().toString().equals("")) {
            result = false;
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