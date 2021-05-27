package it.unimib.letsdrink.ui.drinks.drinks_without_login;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.ui.drinks.FirebaseDBCocktails;

public class CocktailsFragment extends Fragment implements FilterInterface {

    private FirebaseDBCocktails db;
    private View root;
    private CocktailAdapter cocktailAdapter;
    private RecyclerView recyclerView;
    private TextView text;
    private String ananas, arancia, cognac, gin, lime, menta, pesca, rum, soda, vodka;
    private FilterInterface filter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ananas = "Ananas";
        arancia = "Arancia";
        cognac = "Cognac";
        gin = "Gin";
        lime = "Lime";
        menta = "Menta";
        pesca = "Pesca";
        rum = "Rum";
        soda = "Soda";
        vodka = "Vodka";
        root = inflater.inflate(R.layout.fragment_cocktails, container, false);
        recyclerView = root.findViewById(R.id.cocktails_recycler);
        text = root.findViewById(R.id.textNotFound);

        db = new FirebaseDBCocktails();

        db.readCocktails(new FirebaseDBCocktails.DataStatus() {
            @Override
            public void dataIsLoaded(List<Cocktail> listOfCocktails) {
                cocktailAdapter = new CocktailAdapter(listOfCocktails, getContext());
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recyclerView.setAdapter(cocktailAdapter);
                cocktailAdapter.setOnItemClickListener(new CocktailAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Fragment cocktailDetail = CocktailDetailFragment.newInstance(listOfCocktails.get(position).getName(), listOfCocktails.get(position).getMethod(),
                                listOfCocktails.get(position).getIngredients(), listOfCocktails.get(position).getImageUrl());
                        Navigation.findNavController(getView()).navigate(R.id.action_navigation_drinks_to_cocktailDetailFragment);

                    }
                });
            }
        });
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.top_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.search_item:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setQueryHint("Cerca un cocktail");
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        cocktailAdapter.getFilter().filter(newText);
                        if (cocktailAdapter.getNoCocktailsFiltered()) {
                            Log.d("prova", "Nessun cocktail trovato");
                            text.setVisibility(View.VISIBLE);
                            text.setText("Nessun cocktail trovato");
                        }
                        return false;
                    }

                });
                break;
            case R.id.filter_item:
                Log.d("prova", "menu");
                Toast.makeText(getContext(), "menu", Toast.LENGTH_SHORT);
                Fragment filters = FiltersIngredients.newInstance(ananas, arancia, cognac, gin, lime, menta, pesca, rum, soda, vodka, filter);
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_drinks_to_filtersIngredients);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void okButtonClick(boolean valueAnanas, boolean valueArancia, boolean valueCognac, boolean valueGin, boolean valueLime,
                              boolean valueMenta, boolean valuePesca, boolean valueRum, boolean valueSoda, boolean valueVodka) {
        ananas = "Ananas";
        arancia = "Arancia";
        cognac = "Cognac";
        gin = "Gin";
        lime = "Lime";
        menta = "Menta";
        pesca = "Pesca";
        rum = "Rum";
        soda = "Soda";
        vodka = "Vodka";
        if (!valueAnanas)
            ananas = "no";
        if (!valueArancia)
            arancia = "no";
        if (!valueCognac)
            cognac = "no";
        if (!valueGin)
            gin = "no";
        if (!valueLime)
            lime = "no";
        if (!valueMenta)
            menta = "no";
        if (!valuePesca)
            pesca = "no";
        if (!valueRum)
            rum = "no";
        if (!valueSoda)
            soda = "no";
        if (!valueVodka)
            vodka = "no";
        db.readCocktails(new FirebaseDBCocktails.DataStatus() {
            @Override
            public void dataIsLoaded(List<Cocktail> listOfCocktails) {
                ArrayList<Cocktail> cocktailsListFiltered = new ArrayList<>();
                for (int cont = 0; cont < listOfCocktails.size(); cont++) {
                    final Cocktail cocktail = listOfCocktails.get(cont);
                    ArrayList<String> ingredients = listOfCocktails.get(cont).getIngredients();
                    for (int i = 0; i < ingredients.size(); i++) {
                        if (ingredients.get(i).contains(ananas) || ingredients.get(i).contains(arancia) || ingredients.get(i).contains(cognac) ||
                                ingredients.get(i).contains(gin) || ingredients.get(i).contains(lime) || ingredients.get(i).contains(menta) ||
                                ingredients.get(i).contains(pesca) || ingredients.get(i).contains(rum) || ingredients.get(i).contains(soda) ||
                                ingredients.get(i).contains(vodka)) {
                            cocktailsListFiltered.add(cocktail);
                        }

                    }

                }
                if (cocktailsListFiltered.size() == 0) {
                    Toast.makeText(requireContext(), "Nessun cocktail trovato con questi ingredienti ", Toast.LENGTH_LONG).show();

                }else{
                    cocktailAdapter.setListOfAllCocktails(cocktailsListFiltered);
                }

            }
        });
    }

}
