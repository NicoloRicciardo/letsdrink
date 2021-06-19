package it.unimib.letsdrink.firebaseDB;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import it.unimib.letsdrink.domain.Cocktail;

public class FirebaseDBCustomDrink {
    private final CollectionReference collezione;
    private final List<Cocktail> listOfCocktails = new ArrayList<>();
    private final String id;

    public interface DataStatus {
        void dataIsLoaded(List<Cocktail> cocktailList);
    }

    public FirebaseDBCustomDrink(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collezione = db.collection("Utenti");
        this.id = id;
    }

    public void readCocktails(final FirebaseDBCustomDrink.DataStatus dataStatus) {

        collezione.document(id).collection("customDrink").get()
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

    public void deleteCustomDrink(Cocktail cocktail, final FirebaseDBCustomDrink.DataStatus dataStatus) {
        String name = cocktail.getName();

        collezione.document(id).collection("customDrink").whereEqualTo("name", name).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Cocktail cocktail1 = document.toObject(Cocktail.class);
                    listOfCocktails.remove(cocktail1);
                    document.getReference().delete();
                }
                dataStatus.dataIsLoaded(listOfCocktails);
            }
        });
    }
}
