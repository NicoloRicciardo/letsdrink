package it.unimib.letsdrink.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Cocktail implements Parcelable {
    private String procedimento;
    private String nome;
    private String imageUrl;
    private ArrayList<String> ingredienti;

    public Cocktail(String procedimento, String nome, String imageUrl, ArrayList<String> ingredienti) {
        this.procedimento = procedimento;
        this.nome = nome;
        this.imageUrl = imageUrl;
        this.ingredienti = ingredienti;
    }

    public Cocktail() {
    }

    public String getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(String procedimento) {
        this.procedimento = procedimento;
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

    public ArrayList<String> getIngredienti() {
        return ingredienti;
    }

    public void setIngredienti(ArrayList<String> ingredienti) {
        this.ingredienti = ingredienti;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.procedimento);
        dest.writeString(this.nome);
        dest.writeString(this.imageUrl);
        dest.writeStringList(this.ingredienti);
    }

    public void readFromParcel(Parcel source) {
        this.procedimento = source.readString();
        this.nome = source.readString();
        this.imageUrl = source.readString();
        this.ingredienti = source.createStringArrayList();
    }

    protected Cocktail(Parcel in) {
        this.procedimento = in.readString();
        this.nome = in.readString();
        this.imageUrl = in.readString();
        this.ingredienti = in.createStringArrayList();
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
