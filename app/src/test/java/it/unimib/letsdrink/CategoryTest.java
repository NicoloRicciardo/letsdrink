package it.unimib.letsdrink;

import org.junit.Test;

import it.unimib.letsdrink.domain.Category;

import static org.junit.Assert.assertNotEquals;

public class CategoryTest {
    @Test
    public void equals_test() {
        Category primo = new Category();
        Category secondo = new Category("IBA", "", null);

        primo.setName("Analcolici");

        assertNotEquals(primo, secondo);
    }
}
