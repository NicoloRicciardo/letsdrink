package it.unimib.letsdrink.ui.categories;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.domain.Cocktail;

public class CocktailsFragment extends Fragment {

    FirebaseFirestore db;
    ArrayList<Cocktail> cocktails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*Navigation.findNavController(cocktailsRecycler).getCurrentDestination().setLabel(nomeCategoria);
        View root = inflater.inflate(R.layout.fragment_cocktails, container, false);
        db = FirebaseFirestore.getInstance();
        setHasOptionsMenu(true);

        return root;*/

        View root = inflater.inflate(R.layout.fragment_cocktails, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.cocktails_recycler);
        final FragmentManager fm = requireActivity().getSupportFragmentManager();
        new FirebaseDBCocktails().readCocktails(new FirebaseDBCocktails.DataStatus() {
            @Override
            public void dataIsLoaded(List<Cocktail> listOfCocktails) {
                new RecyclerCocktails().setConfiguration(recyclerView, getContext(), listOfCocktails, fm);
                /*CocktailAdapter cocktailAdapter = new CocktailAdapter(listOfCocktails, getContext());
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                recyclerView.setAdapter(cocktailAdapter);

                cocktailAdapter.setOnItemClickListener(new CocktailAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Log.d("prova", "caio");
                        Fragment cocktailDetail = CocktailDetailFragment.newInstance(listOfCocktails.get(position).getName(), listOfCocktails.get(position).getMethod(),
                                listOfCocktails.get(position).getIngredients(), listOfCocktails.get(position).getImageUrl());
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.hide(getParentFragment()).add(R.id.nav_host_fragment,cocktailDetail);
                        ft.commit();
                        ft.addToBackStack(String.valueOf(cocktailDetail));

                    }
                });*/
            }
        });
        return root;
    }

    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cocktails = new ArrayList<>();
        RecyclerView cocktailsRecycler = view.findViewById(R.id.cocktails_recycler);
        CocktailCardAdapter adapter = new CocktailCardAdapter();
        cocktailsRecycler.setAdapter(adapter);


        Bundle bundle = getArguments();
        Category categoria = bundle.getParcelable("categoria");
        String nomeCategoria = categoria.getName();

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


    } */

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
