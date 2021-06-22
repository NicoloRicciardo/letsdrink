package it.unimib.letsdrink.domain;

import java.util.ArrayList;
import java.util.Objects;

//rappresentazione di un Cocktail
public class Cocktail {

    private String method;
    private String name;
    private String imageUrl;
    private ArrayList<String> ingredients;

    public Cocktail(String method, String name, String imageUrl, ArrayList<String> ingredients) {
        this.method = method;
        this.name = name;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
    }

    public Cocktail() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cocktail cocktail = (Cocktail) o;
        return Objects.equals(name, cocktail.name) &&
                Objects.equals(ingredients, cocktail.ingredients);
    }

}
