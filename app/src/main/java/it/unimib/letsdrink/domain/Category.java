package it.unimib.letsdrink.domain;

import com.google.firebase.firestore.DocumentReference;
import java.util.ArrayList;

//rappresentazione di una categoria
public class Category {
    private String name;
    private String imageUrl;
    private ArrayList<DocumentReference> drinks;

    public Category(String name, String imageUrl, ArrayList<DocumentReference> drinks) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.drinks= drinks;
    }

    public Category() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ArrayList<DocumentReference> getDrinks() {
        return drinks;
    }

}
