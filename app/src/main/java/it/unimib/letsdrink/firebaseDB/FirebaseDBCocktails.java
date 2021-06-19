package it.unimib.letsdrink.firebaseDB;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import it.unimib.letsdrink.domain.Category;
import it.unimib.letsdrink.domain.Cocktail;

public class FirebaseDBCocktails {
    private final FirebaseFirestore db;
    private CollectionReference collezione;
    private final List<Cocktail> listOfCocktails = new ArrayList<>();

    public interface DataStatus {
        void dataIsLoaded(List<Cocktail> cocktailList);
    }

    public FirebaseDBCocktails() {
        db = FirebaseFirestore.getInstance();
        collezione = db.collection("Cocktails");
    }

    public void readCocktails(final FirebaseDBCocktails.DataStatus dataStatus) {

        collezione.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Cocktail cocktail = document.toObject(Cocktail.class);
                            listOfCocktails.add(cocktail);
                        }

                        dataStatus.dataIsLoaded(listOfCocktails);
                    }
                });
    }

    public void readCocktailsCategory(String categoryName, final FirebaseDBCocktails.DataStatus dataStatus) {
        collezione = db.collection("Categorie");
        collezione
                .whereEqualTo("name", categoryName)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Category categoriaDb = document.toObject(Category.class);
                            ArrayList<DocumentReference> cocktailsDb = categoriaDb.getDrinks();
                            for (int i = 0; i < cocktailsDb.size(); i++) {
                                DocumentReference ref = cocktailsDb.get(i);
                                ref.get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                DocumentSnapshot doc = task1.getResult();
                                                if (doc != null && doc.exists()) {
                                                    Cocktail cocktail = doc.toObject(Cocktail.class);
                                                    listOfCocktails.add(cocktail);
                                                }
                                                dataStatus.dataIsLoaded(listOfCocktails);
                                            }
                                        });
                            }
                        }
                    }
                });

    }

}
