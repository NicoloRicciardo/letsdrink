package it.unimib.letsdrink.ui.categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import it.unimib.letsdrink.R;

public class CategoriesFragment extends Fragment {

    private CategoriesViewModel categoriesViewModel;
    FirebaseFirestore db;
    ArrayList<String> nomiCategorie = new ArrayList<>();
    ArrayList<String> immaginiCategorie = new ArrayList<>();
    ArrayList<ArrayList<DocumentReference>> drinksCategoria = new ArrayList<>();
    ArrayList<Category> categorie;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        //View root = inflater.inflate(R.layout.fragment_categories, container, false);
        //final TextView textView = root.findViewById(R.id.text_categories);
        /*categoriesViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        }); */
        db = FirebaseFirestore.getInstance();
        setHasOptionsMenu(true);

        RecyclerView categoryRecycler = (RecyclerView)inflater.inflate( R.layout.fragment_categories, container, false);
        CategoryCardAdapter adapter = new CategoryCardAdapter(getContext(), categorie);

        db.collection("Categorie").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d : list) {
                        Category categoria = d.toObject(Category.class);
                        categorie.add(categoria);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        //readDatawithArrayOfReference();
        //GESTIRE DATI DB E AGGIUNGERLI A GESTIONE ADAPTER
        categoryRecycler.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        categoryRecycler.setLayoutManager(layoutManager);

        adapter.setListener(new CategoryCardAdapter.Listener() {
            public void onClick(int position) {
            }
        });

        return categoryRecycler;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void readDatawithArrayOfReference(){
        db.collection("Categorie").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void  onComplete(@NonNull Task<QuerySnapshot> task)  {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document : task.getResult()){
                                /*
                                nomiCategorie.add(document.getId());
                                immaginiCategorie.add((String) document.get("ImageUrl"));
                                ArrayList<DocumentReference> drinks = (ArrayList<DocumentReference>) document.get("Drinks");
                                drinksCategoria.add(drinks);

                                 */
                            }
                        }
                    }
                });

    }

    private void leggiCategorie() {
        db.collection("Categorie").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d : list) {
                        Category categoria = d.toObject(Category.class);
                        categorie.add(categoria);
                    }

                }
            }
        });
    }

}