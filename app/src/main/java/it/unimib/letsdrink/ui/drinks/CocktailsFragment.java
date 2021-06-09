package it.unimib.letsdrink.ui.drinks;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;

public class CocktailsFragment extends Fragment implements FilterInterface {

    private FirebaseDBCocktails db;
    private View root;
    private CocktailAdapter cocktailAdapter;
    private RecyclerView recyclerView;
    private TextView text;
    private List<Cocktail> cocktailList, cocktailsListFiltered;
    private boolean filtri, listValueDrinks[];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_cocktails, container, false);
        recyclerView = root.findViewById(R.id.cocktails_recycler);
        text = root.findViewById(R.id.textNotFound);

        listValueDrinks = new boolean[10];
        cocktailsListFiltered = new ArrayList<>();

        db = new FirebaseDBCocktails();

        db.readCocktails(new FirebaseDBCocktails.DataStatus() {
            @Override
            public void dataIsLoaded(List<Cocktail> listOfCocktails) {
                cocktailList = listOfCocktails;
                cocktailAdapter = new CocktailAdapter(cocktailList, getContext());
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recyclerView.setAdapter(cocktailAdapter);

                SharedPreferences sharedPref = getActivity().getSharedPreferences("Filtri", Context.MODE_PRIVATE);
                boolean drinksFiltrati = sharedPref.getBoolean("FiltriBoolean", false);
                if (drinksFiltrati) {
                    Set<String> checkedCheckboxSet = sharedPref.getStringSet("Filtri_selezionati", null);
                    String[] nomiFiltri = {"Ananas", "Arancia", "Cognac", "Gin", "Lime", "Menta", "Pesca", "Rum", "Soda", "Vodka"};
                    if (checkedCheckboxSet != null) {
                        for (int i = 0; i < listValueDrinks.length; i++) {
                            if (checkedCheckboxSet.contains(nomiFiltri[i]))
                                listValueDrinks[i] = true;

                        }
                        /*drinkFilter(listValueDrinks[0], listValueDrinks[1], listValueDrinks[2], listValueDrinks[3], listValueDrinks[4],
                                listValueDrinks[5], listValueDrinks[6], listValueDrinks[7], listValueDrinks[8], listValueDrinks[9]);*/
                        drinkFilterArray(listValueDrinks);
                        cocktailAdapter.setListOfCocktails(cocktailsListFiltered);
                        recyclerView.getRecycledViewPool().clear();
                        cocktailAdapter.notifyDataSetChanged();
                    }
                } else {
                    cocktailAdapter.setListOfCocktails(cocktailList);
                    recyclerView.getRecycledViewPool().clear();
                    cocktailAdapter.notifyDataSetChanged();
                }

                cocktailAdapter.setOnItemClickListener(new CocktailAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        List<Cocktail> cocktails = cocktailAdapter.getListOfCocktails();
                        Fragment cocktailDetail = CocktailDetailFragment.newInstance(cocktails.get(position).getName(), cocktails.get(position).getMethod(),
                                cocktails.get(position).getIngredients(), cocktails.get(position).getImageUrl());
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
                            Toast.makeText(getContext(), "Nessun cocktail trovato", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }


                });
                break;
            case R.id.filter_item:
                FiltersIngredients filterDialog = FiltersIngredients.newInstance(this);
                Navigation.findNavController(getView()).navigate(R.id.action_navigation_drinks_to_filtersIngredients);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    /*public void okButtonClick(boolean valueAnanas, boolean valueArancia, boolean valueCognac, boolean valueGin, boolean valueLime,
                              boolean valueMenta, boolean valuePesca, boolean valueRum, boolean valueSoda, boolean valueVodka) {*/
    public void okButtonClick(boolean[] modeDrinks) {
        filtri = false;
        //drinkFilter(valueAnanas, valueArancia, valueCognac, valueGin, valueLime, valueMenta, valuePesca, valueRum, valueSoda, valueVodka);
        drinkFilterArray(modeDrinks);
        saveFiltri();
        if (!filtri) {
            cocktailAdapter.setListOfCocktails(cocktailList);
            recyclerView.getRecycledViewPool().clear();
            cocktailAdapter.notifyDataSetChanged();
        } else {
            if (cocktailsListFiltered.size() == 0) {
                Toast.makeText(requireContext(), "Nessun cocktail trovato con questi ingredienti ", Toast.LENGTH_LONG).show();

            } else {
                cocktailAdapter.setListOfCocktails(cocktailsListFiltered);
                recyclerView.getRecycledViewPool().clear();
                cocktailAdapter.notifyDataSetChanged();
            }
        }

    }

    private void drinkFilterArray(boolean[] modeDrinks) {
        for (int cont = 0; cont < cocktailList.size(); cont++) {
            final Cocktail cocktail = cocktailList.get(cont);
            ArrayList<String> ingredients = cocktailList.get(cont).getIngredients();
            for (int k = 0; k < modeDrinks.length; k++) {
                for (int i = 0; i < ingredients.size(); i++) {
                    if ((k == 0 && modeDrinks[k] && ingredients.get(i).contains("Ananas")) ||
                            (k == 1 && modeDrinks[k] && ingredients.get(i).contains("Arancia")) ||
                            (k == 2 && modeDrinks[k] && ingredients.get(i).contains("Cognac")) ||
                            (k == 3 && modeDrinks[k] && ingredients.get(i).contains("Gin")) ||
                            (k == 4 && modeDrinks[k] && ingredients.get(i).contains("Lime")) ||
                            (k == 5 && modeDrinks[k] && ingredients.get(i).contains("Menta")) ||
                            (k == 6 && modeDrinks[k] && ingredients.get(i).contains("Pesca")) ||
                            (k == 7 && modeDrinks[k] && ingredients.get(i).contains("Rum")) ||
                            (k == 8 && modeDrinks[k] && ingredients.get(i).contains("Soda")) ||
                            (k == 9 && modeDrinks[k] && ingredients.get(i).contains("Vodka"))
                    ) {
                        if (!(cocktailsListFiltered.contains(cocktail))) {
                            cocktailsListFiltered.add(cocktail);
                            filtri = true;
                        }
                    }
                }
            }
        }
    }

    private void saveFiltri() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("Filtri", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("FiltriBoolean", filtri);
        editor.apply();
    }

   /* private void drinkFilter(boolean valueAnanas, boolean valueArancia, boolean valueCognac, boolean valueGin, boolean valueLime, boolean valueMenta, boolean valuePesca,
                             boolean valueRum, boolean valueSoda, boolean valueVodka) {
        for (int cont = 0; cont < cocktailList.size(); cont++) {
            final Cocktail cocktail = cocktailList.get(cont);
            ArrayList<String> ingredients = cocktailList.get(cont).getIngredients();
            for (int i = 0; i < ingredients.size(); i++) {
                if ((valueAnanas && ingredients.get(i).contains("Ananas")) ||
                        (valueArancia && ingredients.get(i).contains("Arancia")) ||
                        (valueCognac && ingredients.get(i).contains("Cognac")) ||
                        (valueGin && ingredients.get(i).contains("Gin")) ||
                        (valueLime && ingredients.get(i).contains("Lime")) ||
                        (valueMenta && ingredients.get(i).contains("Menta")) ||
                        (valuePesca && ingredients.get(i).contains("Pesca")) ||
                        (valueRum && ingredients.get(i).contains("Rum")) ||
                        (valueSoda && ingredients.get(i).contains("Soda")) ||
                        (valueVodka && ingredients.get(i).contains("Vodka"))
                ) {
                    if (!(cocktailsListFiltered.contains(cocktail))) {
                        cocktailsListFiltered.add(cocktail);
                        filtri = true;
                    }
                }
            }
        }
    }*/



}
