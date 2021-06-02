package it.unimib.letsdrink.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.jetbrains.annotations.NotNull;

import it.unimib.letsdrink.R;

public class CustomDrinkFragment extends Fragment {

    private LinearLayout mLayoutIngredientsList;
    private ImageView mAddIngredient;


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
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Navigation.findNavController(getView())
                    .navigate(R.id.action_customDrinkFragment_to_profileFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLayoutIngredientsList = view.findViewById(R.id.layout_ingredients_list);
        mAddIngredient = view.findViewById(R.id.image_add_ingredient);

        mAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });
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