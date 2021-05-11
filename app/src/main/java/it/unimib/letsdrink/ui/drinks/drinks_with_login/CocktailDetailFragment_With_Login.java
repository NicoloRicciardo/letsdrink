package it.unimib.letsdrink.ui.drinks.drinks_with_login;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.ui.drinks.drinks_without_login.CocktailDetailFragment;

public class CocktailDetailFragment_With_Login extends Fragment {

    private static String name;
    private static String method;
    private static String imageUrl;
    private static String ingredienti;
    private static ArrayList<String> ingredients;

    public CocktailDetailFragment_With_Login(){
    }

    public static CocktailDetailFragment_With_Login newInstance(String name, String method, ArrayList<String> ingredients, String imageUrl) {
        CocktailDetailFragment_With_Login fragment = new CocktailDetailFragment_With_Login();
        CocktailDetailFragment_With_Login.name = name;
        CocktailDetailFragment_With_Login.method = method;
        CocktailDetailFragment_With_Login.imageUrl = imageUrl;
        CocktailDetailFragment_With_Login.ingredients = ingredients;
        ingredienti="";

        for (int i = 0; i < ingredients.size(); i++)
            ingredienti += ingredients.get(i) + "\n";


        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cocktail_detail__with__login, container, false);

        TextView txtName = root.findViewById(R.id.cocktail_detail_name_with_login);
        TextView txtIngredients = root.findViewById(R.id.cocktail_detail_ingredients_with_login);
        TextView txtMethod = root.findViewById((R.id.cocktail_detail_method_with_login));
        ImageView img = root.findViewById(R.id.cocktail_detail_image_with_login);
        ImageButton imgBtn= root.findViewById(R.id.love_cocktail_detail);
        txtName.setText(name);
        txtIngredients.setText(ingredienti);
        txtMethod.setText(method);
        Glide.with(getContext()).load(imageUrl).into(img);
        imgBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                imgBtn.setColorFilter(Color.RED);
                Log.d("cuore", "cuore premuto");
            }
        });

        return root;
    }
}