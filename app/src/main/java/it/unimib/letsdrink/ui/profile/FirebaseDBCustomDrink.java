package it.unimib.letsdrink.ui.profile;

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
import it.unimib.letsdrink.ui.drinks.FirebaseDBCocktails;

public class FirebaseDBCustomDrink {
    private FirebaseFirestore db;
    private CollectionReference collezione;
    private List<Cocktail> listOfCocktails = new ArrayList<>();
    private String id;

    public interface DataStatus {
        void dataIsLoaded(List<Cocktail> cocktailList);
    }

    public FirebaseDBCustomDrink(String id) {
        db = FirebaseFirestore.getInstance();
        collezione = db.collection("Utenti");
        this.id = id;
    }

    public void readCocktails(final FirebaseDBCustomDrink.DataStatus dataStatus) {

        collezione.document(id).collection("customDrink").get()
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
