package it.unimib.letsdrink.ui.categories;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.adapter.CocktailCardAdapter;

public class CocktailsFragment extends Fragment {

    FirebaseFirestore db;
    ArrayList<String> nomiCocktails = new ArrayList<>();
    ArrayList<String> immaginiCocktails = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        setHasOptionsMenu(true);

        RecyclerView cocktailsRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_cocktails, container, false);
        CocktailCardAdapter adapter = new CocktailCardAdapter();
        cocktailsRecycler.setAdapter(adapter);


        // interrogazione al db


        adapter.setDati(getContext(), nomiCocktails, immaginiCocktails );
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        cocktailsRecycler.setLayoutManager(layoutManager);

        adapter.setListener(new CocktailCardAdapter.Listener() {
            public void onClick(int position) {
            }
        });



        return cocktailsRecycler;
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
