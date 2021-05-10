package it.unimib.letsdrink.ui.categories;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import it.unimib.letsdrink.R;


public class CocktailDetail extends DialogFragment {
    private static String name;
    private static String method;
    private static ImageView image;
    private static ArrayList<String> ingredients;

    public CocktailDetail() {
    }

    public static CocktailDetail newInstance(String name, String method, ArrayList<String> ingredients, ImageView image) {
        CocktailDetail cocktailExpanded = new CocktailDetail();
        CocktailDetail.name=name;
        CocktailDetail.method=method;
        CocktailDetail.image=image;
        CocktailDetail.ingredients=ingredients;

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
        TextView nameTxw = v.findViewById(R.id.cocktail_name);
        //TextView ingredientsTxw = v.findViewById(R.id.cocktail_ingredients);
        TextView methodTxw = v.findViewById((R.id.cocktail_method));
        ImageView imageView = v.findViewById(R.id.image_cocktail);
        nameTxw.setText(name);
        //ingredientsTxw.setText((CharSequence) ingredients);
        methodTxw.setText(method);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        imageView.setImageBitmap(bitmap);
        return new MaterialAlertDialogBuilder(requireActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert)
                .setView(v)
                .setBackground(new ColorDrawable(Color.TRANSPARENT))
                .create();
    }
}






