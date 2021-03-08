package server;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;


class UserTest {
    User user1;
    User user2;
    User user3;
    @BeforeEach
    void setUp() {
         user1 = new User("Mikkel");
         user2 = new User("Mathias");
    }

    @Test
    void getUsername() {
        assertEquals("Mikkel",user1.getUsername());
        assertNotEquals("Mikkel",user2.getUsername());
    }

    @Test
    void isOnline() {
        assertEquals(false,user1.isOnline());
        assertNotEquals(true,user2.isOnline());
        assertThrows(NullPointerException.class, () -> user3.isOnline());
    }

    @Test
    void setToOnline() {
        assertEquals(false,user1.isOnline());
        user1.setToOnline();
        assertEquals(true,user1.isOnline());
    }

    @Test
    void setToOffline() {
        assertEquals(false,user1.isOnline());
        user1.setToOnline();
        assertEquals(true,user1.isOnline());
        user1.setToOffline();
        assertEquals(false,user1.isOnline());
    }
}