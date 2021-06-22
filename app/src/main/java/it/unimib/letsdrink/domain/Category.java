package it.unimib.letsdrink.domain;

import com.google.firebase.firestore.DocumentReference;
import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

}
