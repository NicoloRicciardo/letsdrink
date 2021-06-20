package it.unimib.letsdrink.firebaseDB;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unimib.letsdrink.domain.Category;

//classe per lettura dalla collezione Categorie su Firebase
public class FirebaseDBCategories {

    private final CollectionReference collezione;
    private final List<Category> listOfCategories = new ArrayList<>();

    //interfaccia per notificare la conclusione della lettura del db
    public interface DataStatus{
        void dataIsLoaded(List<Category> listOfCategories);
    }


    public FirebaseDBCategories() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collezione = db.collection("Categorie");
    }

    //metodo per la lettura delle Categorie presenti nella collection
    public void readCategories(final DataStatus dataStatus){

        //otteniamo tutti i documenti della collezione Categorie
       collezione.get()
                .addOnCompleteListener(task -> {
                    //se l'operazione è andata a buon fine
                    if (task.isSuccessful()) {
                        //scorriamo i documenti ricevuti
                        for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            //ogni documento viene trasformato in un'istanza di Categoria (i campi devono coincidere)
                            Category categoria = document.toObject(Category.class);
                            //aggiungiamo la categoria ottenuta da Firebase all'arraylist di categorie
                            listOfCategories.add(categoria);
                        }
                        //notifica di fine operazione asicrona (lettura firebase)
                        dataStatus.dataIsLoaded(listOfCategories);
                    }

                });
    }

}
