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


import java.util.List;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.ui.drinks.FirebaseDBCocktails;

public class CocktailsFragment extends Fragment {

    private FirebaseDBCocktails db;
    private View root;
    private CocktailAdapter cocktailAdapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_cocktails, container, false);
        recyclerView = root.findViewById(R.id.cocktails_recycler);
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
                        if(cocktailAdapter.getNoCocktailsFiltered())
                            Log.d("prova", "Nessun cocktail trovato");
                        return false;
                    }

                });
                break;
            case R.id.filter_item:
                Log.d("prova", "menu");
                Toast.makeText(getContext(), "menu", Toast.LENGTH_SHORT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    @Override
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
