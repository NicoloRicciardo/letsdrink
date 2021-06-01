package it.unimib.letsdrink.ui.drinks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;

public class CocktailsCategoryFragment extends Fragment {

    private static String name, imageUrl;
    private static ArrayList<DocumentReference> drinks;


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
        ActionBar actionBar= ((AppCompatActivity) requireActivity()).getSupportActionBar();
        actionBar.setTitle(CocktailsCategoryFragment.name);
        actionBar.setDisplayHomeAsUpEnabled(false);
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


}
