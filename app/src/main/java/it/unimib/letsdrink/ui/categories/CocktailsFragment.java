package it.unimib.letsdrink.ui.categories;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.adapter.CocktailCardAdapter;
import it.unimib.letsdrink.domain.Category;
import it.unimib.letsdrink.domain.Cocktail;

public class CocktailsFragment extends Fragment {

    FirebaseFirestore db;
    ArrayList<Cocktail> cocktails = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        setHasOptionsMenu(true);

        RecyclerView cocktailsRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_cocktails, container, false);
        CocktailCardAdapter adapter = new CocktailCardAdapter();
        cocktailsRecycler.setAdapter(adapter);

        Bundle bundle = getArguments();
        Category categoria = bundle.getParcelable("categoria");
        String nomeCategoria = categoria.getName();

        //Navigation.findNavController(cocktailsRecycler).getCurrentDestination().setLabel(nomeCategoria);


        db.collection("Categorie")
                .whereEqualTo("name", nomeCategoria)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Category categoriaDb = document.toObject(Category.class);
                        ArrayList<DocumentReference> cocktailsDb = categoriaDb.getDrinks();
                        for (int i = 0; i < cocktailsDb.size(); i++) {
                            DocumentReference ref = cocktailsDb.get(i);
                            ref.get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot doc = task.getResult();
                                                if (doc != null && doc.exists()) {
                                                    Cocktail cocktail = doc.toObject(Cocktail.class);
                                                    cocktails.add(cocktail);
                                                }
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        });



        adapter.setDati(getContext(), cocktails);
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
