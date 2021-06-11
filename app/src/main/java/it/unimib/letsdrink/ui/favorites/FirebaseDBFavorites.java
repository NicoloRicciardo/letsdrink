package it.unimib.letsdrink.ui.favorites;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import it.unimib.letsdrink.domain.Category;
import it.unimib.letsdrink.domain.Cocktail;
import it.unimib.letsdrink.ui.profile.FirebaseDBCustomDrink;

public class FirebaseDBFavorites {
    private FirebaseFirestore db;
    private CollectionReference collezione;
    private List<Cocktail> listOfCocktails = new ArrayList<>();
    private String id;

    public interface DataStatus {
        void dataIsLoaded(List<Cocktail> cocktailList);
    }

    public FirebaseDBFavorites(String id) {
        db = FirebaseFirestore.getInstance();
        collezione = db.collection("Utenti");
        this.id = id;
    }

    public void addFavoriteCocktail(Cocktail cocktail, final FirebaseDBFavorites.DataStatus dataStatus) {

        collezione.document(id).collection("favoritesCocktails").whereEqualTo("name", cocktail.getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                boolean previoslyAdded = false;
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        previoslyAdded = true;
                    }
                    if (!previoslyAdded) {
                        collezione.document(id).collection("favoritesCocktails").add(cocktail);
                        listOfCocktails.add(cocktail);
                    } else {
                        deleteFavoriteCocktail(cocktail, dataStatus);
                    }
                }
            }
        });
    }

    public void deleteFavoriteCocktail(Cocktail cocktail, final FirebaseDBFavorites.DataStatus dataStatus) {

        collezione.document(id).collection("favoritesCocktails").whereEqualTo("name", cocktail.getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Cocktail cocktail = document.toObject(Cocktail.class);
                        listOfCocktails.remove(cocktail);
                        document.getReference().delete();
                    }
                    dataStatus.dataIsLoaded(listOfCocktails);
                }
            }
        });
    }

    public void readCocktails(final FirebaseDBFavorites.DataStatus dataStatus) {

        collezione.document(id).collection("favoritesCocktails").get()
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

}