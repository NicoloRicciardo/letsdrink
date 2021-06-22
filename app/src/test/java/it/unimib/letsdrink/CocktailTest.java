package it.unimib.letsdrink;

import org.junit.Test;
import java.util.ArrayList;
import it.unimib.letsdrink.domain.Cocktail;
import static org.junit.Assert.*;

public class CocktailTest {
    @Test
    public void equal_test() {
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
