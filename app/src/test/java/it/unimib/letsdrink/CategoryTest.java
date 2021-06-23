package it.unimib.letsdrink;

import org.junit.Before;
import org.junit.Test;
import it.unimib.letsdrink.domain.Category;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CategoryTest {
    Category category;

    @Before
    public void initMethod() {
        category = new Category( "After Dinner", "", null);
    }

    @Test
    public void getNameTest() {
        assertEquals("After Dinner", category.getName());
    }

    @Test
    public void getImageUrlTest() {
        assertEquals("", category.getImageUrl());
    }

    @Test
    public void setNameTest() {
        category.setName("Analcolici");
        assertEquals("Analcolici", category.getName());
    }

    @Test
    public void equalsTest() {
        Category primo = new Category();
        Category secondo = new Category("IBA", "", null);

        primo.setName("Analcolici");

        assertNotEquals(primo, secondo);
    }
}
