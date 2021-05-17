package it.unimib.letsdrink.ui.drinks.drinks_without_login;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.DatePicker;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.ui.drinks.FirebaseDBCocktails;
import it.unimib.letsdrink.ui.drinks.drinks_without_login.CocktailAdapter;
import it.unimib.letsdrink.ui.drinks.drinks_without_login.CocktailDetailFragment;

public class CocktailsFragment extends Fragment {

    private SearchView searchView;
    private String cocktailName;
    private FirebaseDBCocktails db;
    private View root;
    private View snackview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_cocktails, container, false);
        snackview = inflater.inflate(R.layout.fragment_cocktails, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.cocktails_recycler);
        Toolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.top_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onIconClick(item);
                return false;
            }
        });
        db = new FirebaseDBCocktails();
        db.readCocktails(new FirebaseDBCocktails.DataStatus() {
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
                        Navigation.findNavController(getView()).navigate(R.id.action_navigation_drinks_to_cocktailDetailFragment);

                    }
                });
            }
        });

        searchView = root.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cocktailName = searchView.getQuery().toString();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return root;
    }

    private void onIconClick(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.search_item:
                Cocktail cocktail = db.searchCocktail(cocktailName);
                if (cocktail != null) {
                    Fragment cocktailDetail = CocktailDetailFragment.newInstance(cocktail.getName(), cocktail.getMethod(),
                            cocktail.getIngredients(), cocktail.getImageUrl());
                    Navigation.findNavController(getView()).navigate(R.id.action_navigation_drinks_to_cocktailDetailFragment);
                } else {
                    Log.d("Prova","Errore" );
                    Snackbar.make(snackview, "Il cocktail cercato non è presente", Snackbar.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.filter_item:
                /* Filters filters = new Filters(this, concertoMode, pubMode, discoMode);
                filters.show(requireActivity().getSupportFragmentManager(), "Filters"); */

        }
    }

    /* @Override
    public void okButtonClick(boolean valueDisco, boolean valuePub, boolean valueConcerto) {
        mMap.clear();
        discoMode = "Discoteca";
        pubMode = "Pub";
        concertoMode = "Concerto";
        final String date = formatData(mDay, mMonth, mYear);
        if (!valueDisco)
            discoMode = "no";
        if (!valuePub)
            pubMode = "no";
        if (!valueConcerto)
            concertoMode = "no";
        new FirebaseDB_Events().readEvent(new FirebaseDB_Events.DataStatus() {
            @Override
            public void dataIsLoaded(List<EventItem> listOfEvents, List<String> listOfKeys) {
                int numEv = 0;
                for (int cont = 0; cont < listOfEvents.size(); cont++) {
                    final EventItem ev = listOfEvents.get(cont);
                    if ((ev.getEventType().equals(discoMode) || ev.getEventType().equals(concertoMode) ||
                            ev.getEventType().equals(pubMode)) && ev.getDateString().equals(date)) {
                        String add = ev.getStreet();
                        LatLng ll = getPosition(add);
                        assert ll != null;
                        mMap.addMarker(new MarkerOptions().position(ll).title(add));
                        numEv++;
                    }
                }
                if (numEv == 0) {
                    Toast.makeText(requireContext(), "Nessun evento di questo tipo il " + date, Toast.LENGTH_LONG).show();
                }
            }
        });
    } */


}
