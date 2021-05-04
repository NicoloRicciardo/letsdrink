package it.unimib.letsdrink.ui.categories;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.unimib.letsdrink.R;
import it.unimib.letsdrink.adapter.CocktailCardAdapter;
import it.unimib.letsdrink.domain.Category;
import it.unimib.letsdrink.domain.Cocktail;

public class CocktailsFragment extends Fragment {

    FirebaseFirestore db;
    ArrayList<Cocktail> cocktails= new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        setHasOptionsMenu(true);

        RecyclerView cocktailsRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_cocktails, container, false);
        CocktailCardAdapter adapter = new CocktailCardAdapter();
        cocktailsRecycler.setAdapter(adapter);

        Bundle bundle= getArguments();
        Category categoria = bundle.getParcelable("categoria");
        String nomeCategoria = categoria.getNome();
        Log.d("nome", nomeCategoria);

        DocumentReference docCategoria=db.collection("Categorie").document(nomeCategoria);


        docCategoria.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document=task.getResult();
                            ArrayList<DocumentReference> cocktails = (ArrayList<DocumentReference>) document.get("Drinks");
                            for(int i=0; i<cocktails.size(); i++){
                                DocumentReference doc=cocktails.get(i);
                                doc.get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot d= task.getResult();
                                                Cocktail c= new Cocktail();
                                                //c.setImageUrl(d.ge);
                                            }

                                            }

                                        });
                            }


                            adapter.notifyDataSetChanged();
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
