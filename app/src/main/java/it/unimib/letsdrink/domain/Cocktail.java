package it.unimib.letsdrink.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Cocktail implements Parcelable {
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.method);
        dest.writeString(this.name);
        dest.writeString(this.imageUrl);
        dest.writeStringList(this.ingredients);
    }

    public void readFromParcel(Parcel source) {
        this.method = source.readString();
        this.name = source.readString();
        this.imageUrl = source.readString();
        this.ingredients = source.createStringArrayList();
    }

    protected Cocktail(Parcel in) {
        this.method = in.readString();
        this.name = in.readString();
        this.imageUrl = in.readString();
        this.ingredients = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Cocktail> CREATOR = new Parcelable.Creator<Cocktail>() {
        @Override
        public Cocktail createFromParcel(Parcel source) {
            return new Cocktail(source);
        }

        @Override
        public Cocktail[] newArray(int size) {
            return new Cocktail[size];
        }
    };
}
