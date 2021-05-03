package it.unimib.letsdrink.ui.categories;

public class Category {
    private String nome;
    private String imageUrl;

    public Category(String nome, String imageUrl) {
        this.nome = nome;
        this.imageUrl = imageUrl;
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


}
