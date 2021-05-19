package it.unimib.letsdrink.ui.drinks;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import it.unimib.letsdrink.domain.Category;
import it.unimib.letsdrink.domain.Cocktail;

public class FirebaseDBCocktails {
    private FirebaseFirestore db;
    private CollectionReference collezione;
    private List<Cocktail> listOfCocktails = new ArrayList<>();

    public interface DataStatus {
        void dataIsLoaded(List<Cocktail> cocktailList);
    }

    public FirebaseDBCocktails() {
        db = FirebaseFirestore.getInstance();
        collezione = db.collection("Cocktails");
    }

    public void readCocktails(final FirebaseDBCocktails.DataStatus dataStatus) {

        collezione.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Cocktail cocktail = document.toObject(Cocktail.class);
                                listOfCocktails.add(cocktail);
                            }

                            dataStatus.dataIsLoaded(listOfCocktails);
                        }
                    }
                });
    }

    public void readCocktailsCategory(String categoryName, final FirebaseDBCocktails.DataStatus dataStatus) {
        collezione = db.collection("Categorie");
        collezione
                .whereEqualTo("name", categoryName)
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
                                                    listOfCocktails.add(cocktail);
                                                }
                                                dataStatus.dataIsLoaded(listOfCocktails);
                                            }
                                        }
                                    });
                        }
                    }
                }
            }
        });

    }

    public Cocktail searchCocktail(String name) {
        for (int i = 0; i < listOfCocktails.size(); i++) {
            if(listOfCocktails.get(i)!= null && listOfCocktails.get(i).getName()!= null &&
                    listOfCocktails.get(i).getName().equalsIgnoreCase(name)){
                return listOfCocktails.get(i);
            }

        }

        return null;


    }
}
