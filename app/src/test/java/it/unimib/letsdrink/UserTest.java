package it.unimib.letsdrink;

import org.junit.Before;
import org.junit.Test;
import it.unimib.letsdrink.domain.User;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UserTest {
    User user;

    @Before
    public void initMethod() {
        user = new User( "pippo", "18", "pippo@paperino.it", "123");
    }

    @Test
    public void getUsernameTest() {
        assertEquals("pippo", user.getUserName());
    }

    @Test
    public void getAgeTest() {
        assertEquals("18", user.getAge());
    }

    @Test
    public void getEmailTest() {
        assertEquals("pippo@paperino.it", user.getEmail());
    }

    @Test
    public void getUserIDTest() {
        assertEquals("123", user.getUserID());
    }

    @Test
    public void setUserNameTest() {
        user.setUserName("mario rossi");
        assertEquals("mario rossi", user.getUserName());
    }

    @Test
    public void setAgeTest() {
        user.setAge("20");
        assertEquals("20", user.getAge());
    }

    @Test
    public void setEmailTest() {
        user.setEmail("pluto@topolino.com");
        assertEquals("pluto@topolino.com", user.getEmail());
    }

    @Test
    public void setUserIDTest() {
        user.setUserID("456");
        assertEquals("456", user.getUserID());
    }


    @Test
    public void equalsTest() {
        User primo = new User();
        User secondo = new User("Gianni", "18", "gianni@gmail.com", "123456");

        primo.setUserName("Ale");
        primo.setAge("23");
        primo.setEmail("ale@gmail.com");

        assertNotEquals(primo, secondo);

    }
}
