package com.mazeco;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import com.mazeco.models.User;

public class UserTest {
    String USERFIRSTNAME = "Test";
    String USERLASTNAME = "User";

    User testUser;

    @BeforeEach 
    public void constructUser(){
        testUser = new User(USERFIRSTNAME, USERLASTNAME);
    }

    @Test
    public void testGetFirstName(){
        String expected = USERFIRSTNAME;
        String actual = testUser.getFirstName();

        assertEquals(expected, actual, "getFirstName not implemented corrrectly");
    }

    @Test
    public void testGetLastName(){
        String expected = USERLASTNAME;
        String actual = testUser.getLastName();

        assertEquals(expected, actual, "getLastName not implemented corrrectly");
    }

    @Test
    public void testSetFirstName(){
        String override = "Maze";
        String expected = override;
        
        testUser.setFirstName(override);

        assertEquals(expected, testUser.getFirstName(), "setFirstName not implemented corrrectly");
    }

    @Test
    public void testSetLastName(){
        String override = "Co";
        String expected = override;
        testUser.setLastName(override);

        assertEquals(expected, testUser.getLastName(), "setLastName not implemented corrrectly");
    }

    @Test
    public void testToString(){
        String expected = USERFIRSTNAME + "," + USERLASTNAME;
        String actual = testUser.toString();

        assertEquals(expected, actual, "toString not implemented corrrectly");
    }

}
