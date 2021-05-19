package it.unimib.letsdrink.ui.drinks;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import it.unimib.letsdrink.domain.Category;


public class FirebaseDBCategories {

    private FirebaseFirestore db;
    private CollectionReference collezione;
    private List<Category> listOfCategories = new ArrayList<>();

    public interface DataStatus{
        void dataIsLoaded(List<Category> listOfCategories);
    }

    public FirebaseDBCategories() {
        db = FirebaseFirestore.getInstance();
        collezione = db.collection("Categorie");
    }

    public void readCategories(final DataStatus dataStatus){

       collezione.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Category categoria = document.toObject(Category.class);
                                listOfCategories.add(categoria);
                            }

                            dataStatus.dataIsLoaded(listOfCategories);
                        }

                    }
                });
    }

}