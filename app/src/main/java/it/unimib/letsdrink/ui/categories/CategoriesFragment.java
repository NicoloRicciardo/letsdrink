package it.unimib.letsdrink.ui.categories;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.adapter.CategoryCardAdapter;

public class CategoriesFragment extends Fragment {

    FirebaseFirestore db;
    ArrayList<String> nomiCategorie = new ArrayList<>();
    ArrayList<String> immaginiCategorie = new ArrayList<>();
    ArrayList<ArrayList<DocumentReference>> drinksCategoria = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        setHasOptionsMenu(true);

        RecyclerView categoryRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_categories, container, false);
        CategoryCardAdapter adapter = new CategoryCardAdapter();
        categoryRecycler.setAdapter(adapter);

        db.collection("Categorie").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                nomiCategorie.add(document.getId());
                                immaginiCategorie.add((String) document.get("ImageUrl"));
                                ArrayList<DocumentReference> drinks = (ArrayList<DocumentReference>) document.get("Drinks");
                                drinksCategoria.add(drinks);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

        adapter.setDati(getContext(), nomiCategorie, immaginiCategorie);



        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        categoryRecycler.setLayoutManager(layoutManager);

        adapter.setListener(new CategoryCardAdapter.Listener() {
            public void onClick(int position) {
            //Log.d("prova", s);
                ArrayList<DocumentReference> drinks = drinksCategoria.get(position);
                Navigation.findNavController(categoryRecycler).navigate(R.id.action_navigation_categories_to_cocktailsFragment);
            }
        });

        return categoryRecycler;
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