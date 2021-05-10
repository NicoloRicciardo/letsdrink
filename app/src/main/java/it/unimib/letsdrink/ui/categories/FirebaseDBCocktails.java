package it.unimib.letsdrink.ui.categories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

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
}
