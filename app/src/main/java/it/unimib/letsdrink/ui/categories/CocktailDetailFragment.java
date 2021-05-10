package it.unimib.letsdrink.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import it.unimib.letsdrink.R;

public class CocktailDetailFragment extends Fragment {
    private static String name;
    private static String method;
    private static String imageUrl;
    private static String ingredienti = "";
    private static ArrayList<String> ingredients;

    public CocktailDetailFragment(){
    }

    public static CocktailDetailFragment newInstance(String name, String method, ArrayList<String> ingredients, String imageUrl) {
        CocktailDetailFragment fragment = new CocktailDetailFragment();
        CocktailDetailFragment.name = name;
        CocktailDetailFragment.method = method;
        CocktailDetailFragment.imageUrl = imageUrl;
        CocktailDetailFragment.ingredients = ingredients;
        for (int i = 0; i < ingredients.size(); i++)
            ingredienti += ingredients.get(i) + "\n";

        return fragment;

        //Bundle args = new Bundle();
        //fragment.setArguments(args);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cocktail_detail, container, false);

        TextView txtName = root.findViewById(R.id.cocktail_detail_name);
        TextView txtIngredients = root.findViewById(R.id.cocktail_detail_ingredients);
        TextView txtMethod = root.findViewById((R.id.cocktail_detail_method));
        ImageView img = root.findViewById(R.id.cocktail_detail_image);
        txtName.setText(name);
        txtIngredients.setText(ingredienti);
        txtMethod.setText(method);
        Glide.with(getContext()).load(imageUrl).into(img);

        return root;
    }
}
