package it.unimib.letsdrink.firebaseDB;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import it.unimib.letsdrink.domain.Cocktail;

//classe per operazioni CRUD (Create Read Update Delete) sulla sub-collezione favoritesCocktails della collezione Utenti su Firebase
public class FirebaseDBFavorites {
    private final CollectionReference collezione;
    private final List<Cocktail> listOfCocktails = new ArrayList<>();
    private final String id;

    //interfaccia per notificare la conclusione dell'operazione sul db
    public interface DataStatus {
        void dataIsLoaded(List<Cocktail> cocktailList);
    }

    public FirebaseDBFavorites(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        collezione = db.collection("Utenti");
        this.id = id;
    }

    //metodo per l'aggiunta del cocktail passato nella sub-collezione favoritesCocktails
    public void addFavoriteCocktail(Cocktail cocktail, final FirebaseDBFavorites.DataStatus dataStatus) {

          /*di questa collezione(Utenti) andiamo a prendere il documento(Utente) con l'id dell'utente,
         e di questo effettuiamo una query sulla sub-collezione favoritesCocktails i quali drink hanno il nome passato*/
        collezione.document(id).collection("favoritesCocktails").whereEqualTo("name", cocktail.getName()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isPreviouslyAdded = false;
                //scorre il risultato della query
                for (DocumentSnapshot ignored : Objects.requireNonNull(task.getResult())) {
                    isPreviouslyAdded = true;
                }
                if(!isPreviouslyAdded) {
                    collezione.document(id).collection("favoritesCocktails").add(cocktail);
                    listOfCocktails.add(cocktail);
                } else{
                    deleteFavoriteCocktail(cocktail, dataStatus);
                }
            }
        });
    }

    //metodo per l'eliminazione di un drink preferito
    public void deleteFavoriteCocktail(Cocktail cocktail, final FirebaseDBFavorites.DataStatus dataStatus) {

        /*di questa collezione(Utenti) andiamo a prendere il documento(Utente) con l'id dell'utente,
         e di questo effettuiamo una query sulla sub-collezione favoritesCocktails i quali drink hanno il nome passato*/
        collezione.document(id).collection("favoritesCocktails").whereEqualTo("name", cocktail.getName()).get().addOnCompleteListener(task -> {
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

    //metodo per la lettura dei Cocktails presenti nella sub-collection customDrink
    public void readCocktails(final FirebaseDBFavorites.DataStatus dataStatus) {
    /*di questa collezione(Utenti) andiamo a prendere il documento(Utente) con l'id dell'utente,
         e di questo effettuiamo una query sulla collezione favoritesCocktails*/
        collezione.document(id).collection("favoritesCocktails").get()
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

}