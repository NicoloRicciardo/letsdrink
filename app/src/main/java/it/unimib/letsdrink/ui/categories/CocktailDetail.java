package it.unimib.letsdrink.ui.categories;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import it.unimib.letsdrink.R;


public class CocktailDetail extends DialogFragment {
    private static String name;
    private static String method;
    private static String imageUrl;
    private static String ingredienti = "";
    private static ArrayList<String> ingredients;

    public CocktailDetail() {
    }

    public static CocktailDetail newInstance(String name, String method, ArrayList<String> ingredients, String imageUrl) {
        CocktailDetail cocktailExpanded = new CocktailDetail();
        CocktailDetail.name = name;
        CocktailDetail.method = method;
        CocktailDetail.imageUrl = imageUrl;
        CocktailDetail.ingredients = ingredients;
        for (int i = 0; i < ingredients.size(); i++)
         ingredienti += ingredients.get(i) + "\n";

        return cocktailExpanded;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = requireActivity().getLayoutInflater().inflate(R.layout.fragment_cocktail_detail, new LinearLayout(getActivity()), false);
        TextView nameTxw = v.findViewById(R.id.cocktail_detail_name);
        TextView ingredientsTxw = v.findViewById(R.id.cocktail_detail_ingredients);
        TextView methodTxw = v.findViewById((R.id.cocktail_detail_method));
        ImageView imageView = v.findViewById(R.id.cocktail_detail_image);
        nameTxw.setText(name);
        ingredientsTxw.setText(ingredienti);
        methodTxw.setText(method);
        Glide.with(getContext()).load(imageUrl).into(imageView);
        return new MaterialAlertDialogBuilder(requireActivity(), R.style.Theme_AppCompat_Dialog_Alert)
                .setView(v)
                .setBackground(new ColorDrawable(Color.WHITE))
                .create();
    }
}






