package it.unimib.letsdrink.ui.categories;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.letsdrink.domain.Cocktail;

public class RecyclerCocktails {

    void setConfiguration(RecyclerView recyclerView, final Context context, final List<Cocktail> listOfCocktails, final FragmentManager fm) {
        CocktailAdapter cocktailAdapter = new CocktailAdapter(listOfCocktails, context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setAdapter(cocktailAdapter);

        cocktailAdapter.setOnItemClickListener(new CocktailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                DialogFragment fragment = CocktailDetail.newInstance(listOfCocktails.get(position).getName(), listOfCocktails.get(position).getMethod(),
                        listOfCocktails.get(position).getIngredients(), listOfCocktails.get(position).getImageUrl());
                fragment.show(fm, "CocktailDetail");
            }
        });
    }
}
