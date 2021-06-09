package it.unimib.letsdrink.ui.profile;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;

public class CustomDrinkDetailFragment extends Fragment {
    private static String name, method, imageUrl, ingredienti;
    private static ArrayList<String> ingredients;
    private String mUserID;

    public CustomDrinkDetailFragment() {
    }

    public static CustomDrinkDetailFragment newInstance(String name, String method, ArrayList<String> ingredients, String imageUrl) {
        CustomDrinkDetailFragment fragment = new CustomDrinkDetailFragment();
        CustomDrinkDetailFragment.name = name;
        CustomDrinkDetailFragment.method = method;
        CustomDrinkDetailFragment.imageUrl = imageUrl;
        CustomDrinkDetailFragment.ingredients = ingredients;
        CustomDrinkDetailFragment.ingredienti = "";

        for (int i = 0; i < ingredients.size(); i++)
            ingredienti += CustomDrinkDetailFragment.ingredients.get(i) + "\n";


        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_custom_drink_detail, container, false);
        ActionBar actionBar= ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("");
        setHasOptionsMenu(true);

        TextView txtName = root.findViewById(R.id.cocktail_custom_drink_detail_name);
        TextView txtIngredients = root.findViewById(R.id.cocktail_custom_drink_detail_ingredients);
        TextView txtMethod = root.findViewById((R.id.cocktail_custom_drink_detail_method));
        ImageView img = root.findViewById(R.id.cocktail_custom_drink_detail_image);

        txtName.setText(name);
        txtIngredients.setText(ingredienti);
        txtMethod.setText(method);
        Glide.with(getContext()).load(imageUrl).into(img);

        mUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.custom_drink_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_item) {
            Cocktail cocktail = new Cocktail(method, name, imageUrl, ingredients);
            new FirebaseDBCustomDrink(mUserID).deleteCustomDrink(cocktail, new FirebaseDBCustomDrink.DataStatus() {
                @Override
                public void dataIsLoaded(List<Cocktail> cocktailList) {
                    /*cocktailAdapter.setListOfCocktails(cocktailsListFiltered);
                    recyclerView.getRecycledViewPool().clear();
                    cocktailAdapter.notifyDataSetChanged();*/
                }
            });

            Navigation.findNavController(getView())
                    .navigate(R.id.action_customDrinkDetailFragment_to_profileFragment);
        }
        return super.onOptionsItemSelected(item);
    }


}
