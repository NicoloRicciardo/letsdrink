package it.unimib.letsdrink.ui.categories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import it.unimib.letsdrink.adapter.CategoryCardAdapter;
import it.unimib.letsdrink.adapter.CocktailCardAdapter;
import it.unimib.letsdrink.domain.Category;
import it.unimib.letsdrink.domain.Cocktail;

public class CocktailsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Cocktail>> cocktails;
    private FirebaseFirestore db;

    public CocktailsViewModel() {
        cocktails = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
    }

    private void setCocktails(CocktailCardAdapter adapter, String nomeCategoria) {
        ArrayList<Cocktail> cocktailsArray = new ArrayList<>();
        db.collection("Categorie")
                .whereEqualTo("name", nomeCategoria)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d("prova", "prova");
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
                                                    cocktailsArray.add(cocktail);
                                                }
                                                cocktails.setValue(cocktailsArray);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        });
    }

    public LiveData<ArrayList<Cocktail>> getCocktails(CocktailCardAdapter adapter, String nomeCategoria){
        setCocktails(adapter, nomeCategoria);
        return cocktails;
    }

    public LiveData<String> getCocktail(int position) {
        ArrayList<Cocktail> cocktailsArray = cocktails.getValue();
        MutableLiveData<String> nameCocktail = null;
        nameCocktail.setValue(cocktailsArray.get(position).getName());
        return nameCocktail;

    }
}
