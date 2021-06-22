package it.unimib.letsdrink;

import org.junit.Test;

import it.unimib.letsdrink.domain.User;

import static org.junit.Assert.assertNotEquals;

public class UserTest {
    @Test
    public void equals_test() {
        User primo = new User();
        User secondo = new User("Gianni", "18", "gianni@gmail.com", "123456");

        primo.setUserName("Ale");
        primo.setAge("23");
        primo.setEmail("ale@gmail.com");

        assertNotEquals(primo, secondo);

    }
}
