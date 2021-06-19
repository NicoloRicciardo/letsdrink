package it.unimib.letsdrink.domain;

import java.util.ArrayList;

/*classe relativa al cocktail generico, composto da:
procedimento (method), nome (name), immagine (ImageUrl) e ingredienti (ingredients)*/
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

}
