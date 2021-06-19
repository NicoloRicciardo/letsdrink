package it.unimib.letsdrink.firebaseDB;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import it.unimib.letsdrink.domain.Category;

public class FirebaseDBCategories {

    private final CollectionReference collezione;
    private final List<Category> listOfCategories = new ArrayList<>();

    public interface DataStatus{
        void dataIsLoaded(List<Category> listOfCategories);
    }

    public FirebaseDBCategories() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collezione = db.collection("Categorie");
    }

    public void readCategories(final DataStatus dataStatus){

       collezione.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Category categoria = document.toObject(Category.class);
                            listOfCategories.add(categoria);
                        }

                        dataStatus.dataIsLoaded(listOfCategories);
                    }

                });
    }

}
