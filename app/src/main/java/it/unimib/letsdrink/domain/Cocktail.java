package it.unimib.letsdrink.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.util.ArrayList;

public class Cocktail {
    private String method;
    private String name;
    private String imageUrl;
    private ArrayList<String> ingredients;
    private ImageView image;

    public Cocktail(String method, String name, String imageUrl, ArrayList<String> ingredients, ImageView image) {
        this.method = method;
        this.name = name;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.image=image;
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

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }


}
