package it.unimib.letsdrink.ui.home;

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

//fragment del singolo cocktail
public class CocktailDetailFragment extends Fragment {
    private static String name, method, imageUrl;
    private static StringBuilder ingredienti;
    private static ArrayList<String> ingredients;


    public CocktailDetailFragment() {
    }

    public static void newInstance(String name, String method, ArrayList<String> ingredients, String imageUrl) {
        new CocktailDetailFragment();
        CocktailDetailFragment.name = name;
        CocktailDetailFragment.method = method;
        CocktailDetailFragment.imageUrl = imageUrl;
        CocktailDetailFragment.ingredients = ingredients;
        CocktailDetailFragment.ingredienti = new StringBuilder();

        for (int i = 0; i < ingredients.size(); i++)
            ingredienti.append(CocktailDetailFragment.ingredients.get(i)).append("\n");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cocktail_detail, container, false);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle(name);
        TextView txtIngredients = root.findViewById(R.id.cocktail_detail_ingredients);
        TextView txtMethod = root.findViewById((R.id.cocktail_detail_method));
        ImageView img = root.findViewById(R.id.cocktail_detail_image);
        txtIngredients.setText(ingredienti);
        txtMethod.setText(method);
        Glide.with(requireContext()).load(imageUrl).into(img);

        return root;
    }

}
