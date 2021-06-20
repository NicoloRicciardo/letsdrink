package it.unimib.letsdrink.firebaseDB;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unimib.letsdrink.domain.Category;
import it.unimib.letsdrink.domain.Cocktail;

//classe per lettura dalla collezione Cocktails su Firebase
public class FirebaseDBCocktails {
    private final FirebaseFirestore db;
    private CollectionReference collezione;
    private final List<Cocktail> listOfCocktails = new ArrayList<>();

    //interfaccia per notificare la conclusione della lettura del db
    public interface DataStatus {
        void dataIsLoaded(List<Cocktail> cocktailList);
    }

    public FirebaseDBCocktails() {
        db = FirebaseFirestore.getInstance();
        collezione = db.collection("Cocktails");
    }

    //metodo per la lettura dei Cocktails presenti nella collection
    public void readCocktails(final FirebaseDBCocktails.DataStatus dataStatus) {

        collezione.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Cocktail cocktail = document.toObject(Cocktail.class);
                            listOfCocktails.add(cocktail);
                        }

                        dataStatus.dataIsLoaded(listOfCocktails);
                    }
                });
    }

    //metodo per la lettura dei cocktails della relativa categoria passata
    public void readCocktailsCategory(String categoryName, final FirebaseDBCocktails.DataStatus dataStatus) {
        collezione = db.collection("Categorie");
        //effettuiamo la query per ottenere la lista dei cocktail della categoria passata
        collezione
                //il primo parametro del metodo whereEqualTo è il nome del campo nella collection, il secondo il valore confrontato
                .whereEqualTo("name", categoryName)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Category categoriaDb = document.toObject(Category.class);
                            assert categoriaDb != null;
                            //si scorre l'arraylist di documentReference (Cocktails) relativi alla categoria
                            ArrayList<DocumentReference> cocktailsDb = categoriaDb.getDrinks();
                            for (int i = 0; i < cocktailsDb.size(); i++) {
                                DocumentReference ref = cocktailsDb.get(i);
                                //si effettua la lettura del documento effettivo dal documentReference
                                ref.get()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                //si ottiene il documento
                                                DocumentSnapshot doc = task1.getResult();
                                                if (doc != null && doc.exists()) {
                                                    //ogni documento viene trasformato in un'istanza di Cocktail (i campi devono coincidere)
                                                    Cocktail cocktail = doc.toObject(Cocktail.class);
                                                    //aggiungiamo il cocktail ottenuto da Firebase all'arraylist di cocktails
                                                    listOfCocktails.add(cocktail);
                                                }
                                                //notifica di fine operazione asicrona (lettura firebase)
                                                dataStatus.dataIsLoaded(listOfCocktails);
                                            }
                                        });
                            }
                        }
                    }
                });

    }

}
