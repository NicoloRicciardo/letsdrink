package it.unimib.letsdrink.ui.drinks.drinks_without_login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.ui.drinks.FirebaseDBCocktails;

public class CocktailsCategoryFragment extends Fragment {

    private static String name;
    private static ArrayList<DocumentReference> drinks;
    private static String imageUrl;

    public CocktailsCategoryFragment() {
    }

    public static CocktailsCategoryFragment newInstance(String name, String imageUrl, ArrayList<DocumentReference> drinks) {
        CocktailsCategoryFragment cocktailsCategoryFragment = new CocktailsCategoryFragment();
        cocktailsCategoryFragment.name = name;
        cocktailsCategoryFragment.drinks = drinks;
        cocktailsCategoryFragment.imageUrl = imageUrl;

        return cocktailsCategoryFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cocktails, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.cocktails_recycler);
        setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        String categoryName = bundle.getString("name");
        new FirebaseDBCocktails().readCocktailsCategory(categoryName, new FirebaseDBCocktails.DataStatus() {
            @Override
            public void dataIsLoaded(List<Cocktail> listOfCocktails) {
                CocktailAdapter cocktailAdapter = new CocktailAdapter(listOfCocktails, getContext());
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recyclerView.setAdapter(cocktailAdapter);

                cocktailAdapter.setOnItemClickListener(new CocktailAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Fragment cocktailDetail = CocktailDetailFragment.newInstance(listOfCocktails.get(position).getName(), listOfCocktails.get(position).getMethod(),
                                listOfCocktails.get(position).getIngredients(), listOfCocktails.get(position).getImageUrl());
                        Navigation.findNavController(getView()).navigate(R.id.action_cocktailsCategoryFragment_to_cocktailDetailFragment);

                    }
                });
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
