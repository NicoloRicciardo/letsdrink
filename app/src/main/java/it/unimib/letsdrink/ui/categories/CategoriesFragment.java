package it.unimib.letsdrink.ui.categories;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.gson.internal.$Gson$Preconditions;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        //prova();

        RecyclerView categoryRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_categories, container, false);
        db.collection("Categorie").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int i = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                nomiCategorie.add(i,document.getId());
                                immaginiCategorie.add(i,(String) document.get("ImageUrl"));
                                ArrayList<DocumentReference> drinks = (ArrayList<DocumentReference>) document.get("Drinks");
                                drinksCategoria.add(i,drinks);
                                i++;
                            }
                        }
                    }
                });


        CategoryCardAdapter adapter = new CategoryCardAdapter(getContext(), nomiCategorie, immaginiCategorie);


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
        inflater.inflate(R.menu.top_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void leggiCategorie() {


    }

    /*private void prova() {
        CollectionReference categories = db.collection("Categorie");
        DocumentReference docRef = db.collection("Categorie").document("After Dinner");

        Source source = Source.SERVER;
        Log.d("Prova", "Exit");
        docRef.get(source).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Toast.makeText(getContext(), document.getId(), Toast.LENGTH_SHORT).show();
                    Log.d("Prova", document.getId());
                } else {
                    Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT);
                }

            }
        });


    }*/

}