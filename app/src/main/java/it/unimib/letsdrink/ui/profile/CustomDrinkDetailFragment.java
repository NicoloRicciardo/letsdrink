package it.unimib.letsdrink.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import it.unimib.letsdrink.R;

public class CustomDrinkDetailFragment extends Fragment {
    private static String name, method, imageUrl;
    private static StringBuilder ingredienti;
    private static ArrayList<String> ingredients;

    public CustomDrinkDetailFragment() {
    }

    public static CustomDrinkDetailFragment newInstance(String name, String method, ArrayList<String> ingredients, String imageUrl) {
        CustomDrinkDetailFragment fragment = new CustomDrinkDetailFragment();
        CustomDrinkDetailFragment.name = name;
        CustomDrinkDetailFragment.method = method;
        CustomDrinkDetailFragment.imageUrl = imageUrl;
        CustomDrinkDetailFragment.ingredients = ingredients;
        CustomDrinkDetailFragment.ingredienti = new StringBuilder();

        for (int i = 0; i < ingredients.size(); i++)
            ingredienti.append(CustomDrinkDetailFragment.ingredients.get(i)).append("\n");

        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_custom_drink_detail, container, false);
        ActionBar actionBar= ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle(name);
        setHasOptionsMenu(true);

        TextView txtIngredients = root.findViewById(R.id.cocktail_custom_drink_detail_ingredients);
        TextView txtMethod = root.findViewById((R.id.cocktail_custom_drink_detail_method));
        ImageView img = root.findViewById(R.id.cocktail_custom_drink_detail_image);

        txtIngredients.setText(ingredienti);
        txtMethod.setText(method);
        Glide.with(getContext()).load(imageUrl).into(img);

        return root;
    }

}
