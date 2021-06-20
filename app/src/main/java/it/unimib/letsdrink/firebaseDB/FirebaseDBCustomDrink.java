package it.unimib.letsdrink.firebaseDB;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.unimib.letsdrink.domain.Cocktail;

//classe per operazioni CRUD sulla sub-collezione customDrinks della collezione Utenti su Firebase
public class FirebaseDBCustomDrink {
    private final CollectionReference collezione;
    private final List<Cocktail> listOfCocktails = new ArrayList<>();
    private final String id;

    //interfaccia per notificare la conclusione dell'operazione sul db
    public interface DataStatus {
        void dataIsLoaded(List<Cocktail> cocktailList);
    }

    public FirebaseDBCustomDrink(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collezione = db.collection("Utenti");
        this.id = id;
    }

    //metodo per la lettura dei Cocktails presenti nella sub-collection customDrink
    public void readCocktails(final FirebaseDBCustomDrink.DataStatus dataStatus) {
    /*di questa collezione(Utenti) andiamo a prendere il documento(Utente) con l'id dell'utente,
         e di questo effettuiamo una query sulla collezione CustomDrink*/
        collezione.document(id).collection("customDrink").get()
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

    //metodo per l'eliminazione di un CustomDrink
    public void deleteCustomDrink(Cocktail cocktail, final FirebaseDBCustomDrink.DataStatus dataStatus) {
        String name = cocktail.getName();

        /*di questa collezione(Utenti) andiamo a prendere il documento(Utente) con l'id dell'utente,
         e di questo effettuiamo una query sulla sub-collezione CustomDrink i quali drink hanno il nome passato*/
        collezione.document(id).collection("customDrink").whereEqualTo("name", name).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Cocktail cocktail1 = document.toObject(Cocktail.class);
                    //rimuoviamo il cocktail dalla lista
                    listOfCocktails.remove(cocktail1);
                    //rimuoviamo il cocktail dalla collection
                    document.getReference().delete();
                }
                dataStatus.dataIsLoaded(listOfCocktails);
            }
        });
    }
}
