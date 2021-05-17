package it.unimib.letsdrink.ui.drinks.drinks_with_login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.ui.drinks.FirebaseDBCocktails;
import it.unimib.letsdrink.ui.drinks.drinks_without_login.CocktailAdapter;
import it.unimib.letsdrink.ui.drinks.drinks_without_login.CocktailDetailFragment;


public class CocktailsFragment_With_Login extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cocktails, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.cocktails_recycler);
        setHasOptionsMenu(true);
        new FirebaseDBCocktails().readCocktails(new FirebaseDBCocktails.DataStatus() {
            @Override
            public void dataIsLoaded(List<Cocktail> listOfCocktails) {
                CocktailAdapter_With_Login cocktailAdapter = new CocktailAdapter_With_Login(listOfCocktails, getContext());
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recyclerView.setAdapter(cocktailAdapter);

                cocktailAdapter.setOnItemClickListener(new CocktailAdapter_With_Login.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Fragment cocktailDetail = CocktailDetailFragment_With_Login.newInstance(listOfCocktails.get(position).getName(), listOfCocktails.get(position).getMethod(),
                                listOfCocktails.get(position).getIngredients(), listOfCocktails.get(position).getImageUrl());
                        Navigation.findNavController(getView()).navigate(R.id.action_navigation_drinks_to_cocktailDetailFragment_With_Login);

                    }
                });
            }
        });
        return root;
    }

}