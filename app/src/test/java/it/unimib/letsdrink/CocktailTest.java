package it.unimib.letsdrink;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import it.unimib.letsdrink.domain.Cocktail;
import static org.junit.Assert.*;

public class CocktailTest {
    Cocktail cocktail;

    @Before
    public void initMethod() {
        ArrayList<String> ingredienti = new ArrayList<>();
        ingredienti.add("Pesca");
        cocktail = new Cocktail( "aggiungi gli ingredienti", "Vodka", "", ingredienti);
    }

    @Test
    public void getNameTest() {
        assertEquals("Vodka", cocktail.getName());
    }

    @Test
    public void getMethodTest() {
        assertEquals("aggiungi gli ingredienti", cocktail.getMethod());
    }

    @Test
    public void getImageUrlTest() {
        assertEquals("", cocktail.getImageUrl());
    }

    @Test
    public void getIngredientiTest() {
        ArrayList<String> ingredienti = new ArrayList<>();
        ingredienti.add("Pesca");
        assertEquals(ingredienti, cocktail.getIngredients());
    }

    @Test
    public void setNameTest() {
        cocktail.setName("Alice Cocktail");
        assertEquals("Alice Cocktail", cocktail.getName());
    }

    @Test
    public void setMethodTest() {
        cocktail.setMethod("inserisci gli ingredienti");
        assertEquals("inserisci gli ingredienti", cocktail.getMethod());
    }

    @Test
    public void setImageUrlTest() {
        cocktail.setImageUrl("/prova");
        assertEquals("/prova", cocktail.getImageUrl());
    }

    @Test
    public void setIngredientsTest() {
        ArrayList<String> ingr = new ArrayList<>();
        ingr.add("Limone");
        cocktail.setIngredients(ingr);
        assertEquals(ingr, cocktail.getIngredients());
    }

    @Test
    public void equalTest() {
        Cocktail primo = new Cocktail();
        Cocktail secondo = new Cocktail();
        Cocktail terzo = new Cocktail();

        ArrayList<String> ingredientiPrimo = new ArrayList<>();
        ArrayList<String> ingredientiSecondo = new ArrayList<>();
        ArrayList<String> ingredientiTerzo = new ArrayList<>();

        primo.setName("Caipiroska");
        secondo.setName("Caipiroska");
        terzo.setName("Mojito");

        ingredientiPrimo.add("Ananas");
        ingredientiSecondo.add("Ananas");
        ingredientiTerzo.add("Banana");

        primo.setIngredients(ingredientiPrimo);
        secondo.setIngredients(ingredientiSecondo);
        terzo.setIngredients(ingredientiTerzo);

        assertEquals(primo, secondo);
        assertNotEquals(primo, terzo);
        assertNotEquals(secondo, terzo);
    }
}
