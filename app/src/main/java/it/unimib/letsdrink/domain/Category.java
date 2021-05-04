package it.unimib.letsdrink.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class Category implements Parcelable {
    private String nome;
    private String imageUrl;
    private ArrayList<DocumentReference> drinks;

    public Category(String nome, String imageUrl, ArrayList<DocumentReference> drinks) {
        this.nome = nome;
        this.imageUrl = imageUrl;
        this.drinks= drinks;
    }

    public Category() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<DocumentReference> getDrinks() {
        return drinks;
    }

    public void setDrinks(ArrayList<DocumentReference> drinks) {
        this.drinks = drinks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nome);
        dest.writeString(this.imageUrl);
        dest.writeList(this.drinks);
    }

    public void readFromParcel(Parcel source) {
        this.nome = source.readString();
        this.imageUrl = source.readString();
        this.drinks = new ArrayList<DocumentReference>();
        source.readList(this.drinks, DocumentReference.class.getClassLoader());
    }

    protected Category(Parcel in) {
        this.nome = in.readString();
        this.imageUrl = in.readString();
        this.drinks = new ArrayList<DocumentReference>();
        in.readList(this.drinks, DocumentReference.class.getClassLoader());
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
