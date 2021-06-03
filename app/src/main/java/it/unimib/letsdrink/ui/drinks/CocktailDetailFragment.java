package it.unimib.letsdrink.ui.drinks;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import it.unimib.letsdrink.R;

public class CocktailDetailFragment extends Fragment {
    private static String name, method, imageUrl, ingredienti;
    private static ArrayList<String> ingredients;
    private FirebaseUser currentUser;


    public CocktailDetailFragment() {
    }

    public static CocktailDetailFragment newInstance(String name, String method, ArrayList<String> ingredients, String imageUrl) {
        CocktailDetailFragment fragment = new CocktailDetailFragment();
        CocktailDetailFragment.name = name;
        CocktailDetailFragment.method = method;
        CocktailDetailFragment.imageUrl = imageUrl;
        CocktailDetailFragment.ingredients = ingredients;
        CocktailDetailFragment.ingredienti = "";

        for (int i = 0; i < ingredients.size(); i++)
            ingredienti += CocktailDetailFragment.ingredients.get(i) + "\n";


        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cocktail_detail, container, false);
        ActionBar actionBar= ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        TextView txtName = root.findViewById(R.id.cocktail_detail_name);
        TextView txtIngredients = root.findViewById(R.id.cocktail_detail_ingredients);
        TextView txtMethod = root.findViewById((R.id.cocktail_detail_method));
        ImageView img = root.findViewById(R.id.cocktail_detail_image);
        ImageButton imgBtn = root.findViewById(R.id.love_cocktail_detail);
        txtName.setText(name);
        txtIngredients.setText(ingredienti);
        txtMethod.setText(method);
        Glide.with(getContext()).load(imageUrl).into(img);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser!=null) {
                    imgBtn.setColorFilter(Color.RED);
                    Log.d("cuore", "cuore premuto");
                }else{
                    Toast.makeText(getContext(),"Devi essere loggato per salvare un cocktail", Toast.LENGTH_SHORT ).show();
                }

            }
        });

        return root;
    }


}
