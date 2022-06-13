package com.mazeco;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.*;

import com.mazeco.models.MazeModel;
import com.mazeco.models.MazeRecord;
import com.mazeco.models.User;


public class MazeRecordTest 
{
    MazeRecord mazeRecord;
    MazeModel mazeModel;

    String MAZENAME = "Test Maze";
    String USERFIRSTNAME = "Test";
    String USERLASTNAME = "User";

    @BeforeEach
    public void constructMazeRecord(){
        mazeRecord = new MazeRecord(MAZENAME, new User(USERFIRSTNAME, USERLASTNAME), mazeModel, null, null);
    }

    @Test
    public void getNameTest(){
        String expected = MAZENAME;
        String actual = mazeRecord.getName();

        assertEquals(expected, actual, "getName not implemented correctly");
    }

    @Test
    public void setNameTest(){
        String override = "Another Maze Name";
        String expected = override;

        mazeRecord.setName(override);

        String actual = mazeRecord.getName();

        assertEquals(expected, actual, "setName not implemented correctly");
    }

    @Test
    public void getAuthor(){
        User expected = new User(USERFIRSTNAME, USERLASTNAME);
        User actual = mazeRecord.getAuthor();
        assertAll(()-> {
            assertEquals(USERFIRSTNAME, actual.getFirstName(), "getAuthor not implemented correctly");
            assertEquals(USERLASTNAME, actual.getLastName(), "getAuthor not implemented correctly");
        });
    }

    @Test
    public void testGetDateTimeCreated(){
        Instant instant = Instant.now();
        ZoneId zid = ZoneId.systemDefault();

        ZonedDateTime temp = instant.atZone(zid);
        ZonedDateTime expected = instant.atZone(zid).truncatedTo(ChronoUnit.HOURS)
        .plusMinutes(15 * (temp.getMinute() / 15));

        ZonedDateTime actual = mazeRecord.getDateTimeCreated().truncatedTo(ChronoUnit.HOURS)
        .plusMinutes(15 * (mazeRecord.getDateTimeCreated().getMinute() / 15));

        assertEquals(expected, actual, "getDateTimeCreated not implemented correctly");
    }

    @Test
    public void testGetDateTimeModified(){
        Instant instant = Instant.now();
        ZoneId zid = ZoneId.systemDefault();

        ZonedDateTime temp = instant.atZone(zid);
        ZonedDateTime expected = instant.atZone(zid).truncatedTo(ChronoUnit.HOURS)
        .plusMinutes(15 * (temp.getMinute() / 15));

        ZonedDateTime actual = mazeRecord.getDateTimeCreated().truncatedTo(ChronoUnit.HOURS)
        .plusMinutes(15 * (mazeRecord.getDateTimeModified().getMinute() / 15));

        assertEquals(expected, actual, "getDateTimeModified not implemented correctly");
    }

    @Test
    public void testGetMazeModel(){
        MazeModel expected = mazeModel;
        MazeModel actual = mazeRecord.getMazeModel();

        assertEquals(expected, actual, "getMazeModel not implemented correctly");
    }

    @Test
    public void testToString(){
        String expected = MAZENAME + " - " + USERFIRSTNAME + "," + USERLASTNAME;
        String actual = mazeRecord.toString();

        assertEquals(expected, actual, "toString not implemented correctly");
    }
}
