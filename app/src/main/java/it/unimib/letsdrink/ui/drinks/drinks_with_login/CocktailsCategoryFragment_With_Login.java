package it.unimib.letsdrink.ui.drinks.drinks_with_login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.ui.drinks.FirebaseDBCocktails;
import it.unimib.letsdrink.ui.drinks.drinks_without_login.CocktailAdapter;
import it.unimib.letsdrink.ui.drinks.drinks_without_login.CocktailDetailFragment;
import it.unimib.letsdrink.ui.drinks.drinks_without_login.CocktailsCategoryFragment;


public class CocktailsCategoryFragment_With_Login extends Fragment {

    private static String name;
    private static ArrayList<DocumentReference> drinks;
    private static String imageUrl;

    public CocktailsCategoryFragment_With_Login() {
    }

    public static CocktailsCategoryFragment_With_Login newInstance(String name, String imageUrl, ArrayList<DocumentReference> drinks) {
        CocktailsCategoryFragment_With_Login cocktailsCategoryFragment = new CocktailsCategoryFragment_With_Login();
        cocktailsCategoryFragment.name = name;
        cocktailsCategoryFragment.drinks = drinks;
        cocktailsCategoryFragment.imageUrl = imageUrl;

        return cocktailsCategoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cocktails, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.cocktails_recycler);
        final FragmentManager fm = requireActivity().getSupportFragmentManager();
        Bundle bundle = this.getArguments();
        String categoryName = bundle.getString("name");
        new FirebaseDBCocktails().readCocktailsCategory(categoryName, new FirebaseDBCocktails.DataStatus() {
            @Override
            public void dataIsLoaded(List<Cocktail> listOfCocktails) {
                CocktailAdapter_With_Login cocktailAdapter = new CocktailAdapter_With_Login(listOfCocktails, getContext());
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recyclerView.setAdapter(cocktailAdapter);

                cocktailAdapter.setOnItemClickListener(new CocktailAdapter_With_Login.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        //Log.d("prova", "caio");
                        Fragment cocktailDetail = CocktailDetailFragment_With_Login.newInstance(listOfCocktails.get(position).getName(), listOfCocktails.get(position).getMethod(),
                                listOfCocktails.get(position).getIngredients(), listOfCocktails.get(position).getImageUrl());
                        FragmentTransaction ft = fm.beginTransaction();
                        //ft.hide(getParentFragment());
                        ft.add(R.id.nav_host_fragment,cocktailDetail);
                        ft.commit();
                        ft.addToBackStack(String.valueOf(cocktailDetail));

                    }
                });
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}