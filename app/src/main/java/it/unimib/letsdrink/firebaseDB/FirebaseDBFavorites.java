package it.unimib.letsdrink.firebaseDB;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import it.unimib.letsdrink.domain.Cocktail;

public class FirebaseDBFavorites {
    private final CollectionReference collezione;
    private final List<Cocktail> listOfCocktails = new ArrayList<>();
    private final String id;

    public interface DataStatus {
        void dataIsLoaded(List<Cocktail> cocktailList);
    }

    public FirebaseDBFavorites(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collezione = db.collection("Utenti");
        this.id = id;
    }

    public void addFavoriteCocktail(Cocktail cocktail, final FirebaseDBFavorites.DataStatus dataStatus) {

        collezione.document(id).collection("favoritesCocktails").whereEqualTo("name", cocktail.getName()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                collezione.document(id).collection("favoritesCocktails").add(cocktail);
                listOfCocktails.add(cocktail);
            }
            else{
                deleteFavoriteCocktail(cocktail, dataStatus);
            }
        });
    }

    public void deleteFavoriteCocktail(Cocktail cocktail, final FirebaseDBFavorites.DataStatus dataStatus) {

        collezione.document(id).collection("favoritesCocktails").whereEqualTo("name", cocktail.getName()).get().addOnCompleteListener(task -> {
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

    public void readCocktails(final FirebaseDBFavorites.DataStatus dataStatus) {

        collezione.document(id).collection("favoritesCocktails").get()
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

}