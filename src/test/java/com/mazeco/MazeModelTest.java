package com.mazeco;

import static org.junit.jupiter.api.Assertions.*;

import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;

import org.junit.jupiter.api.*;

import java.awt.*;


public class MazeModelTest {
    private static final int TEST_WIDTH = 10;
    private static final int TEST_HEIGHT = 10;
    private static final int TEST_START = 1;
    private static final int TEST_END = 7;


    private static final int MIN_WIDTH = 10;
    private static final int MIN_HEIGHT = 10;
    private static final int MAX_WIDTH = 100;
    private static final int MAX_HEIGHT = 100;

    MazeModel mazeModel;
    MazeModel mazeModelStartEnd;

    @BeforeEach
    public void constructMazeModel(){
        mazeModel = new MazeModel(TEST_WIDTH, TEST_HEIGHT);
        mazeModelStartEnd = new MazeModel(TEST_WIDTH, TEST_HEIGHT, TEST_START, TEST_END);
    }

    @Test
    public void constructorShouldThrowIllegalArgumentException() {
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () -> {
                mazeModel = new MazeModel(MIN_WIDTH - 1, MIN_HEIGHT - 1);
            });

            assertThrows(IllegalArgumentException.class, () -> {
                mazeModel = new MazeModel(MAX_WIDTH + 1, MAX_HEIGHT + 1);
            });
        });

    }

    @Test
    public void testGetWidth() {
        assertEquals(TEST_WIDTH, mazeModel.getWidth(), "getWidth not implemented correctly");
    }

    @Test
    public void testGetHeight() {
        assertEquals(TEST_HEIGHT, mazeModel.getHeight(), "getHeight not implemented correctly");
    }

    @Test
    public void testGetBlock() {
        mazeModel.setBlock(Block.BLANK, 0, 0);
        assertEquals(Block.BLANK, mazeModel.getBlock(0, 0), "getBlock not implemented correctly");
    }

    @Test
    public void testResetData() {
        mazeModelStartEnd.resetData();

        for (int i = 0; i < TEST_HEIGHT; i++) {
            for (int j = 0; j < TEST_WIDTH; j++) {
                if (i == 0 && j == TEST_START)
                    assertEquals(Block.START, mazeModelStartEnd.getBlock(j, i), "resetData did not reset start properly");
                else if (i == TEST_HEIGHT - 1 && j == TEST_END)
                    assertEquals(Block.END, mazeModelStartEnd.getBlock(j, i), "resetData did not reset end properly");
                else
                    assertEquals(Block.BLANK, mazeModelStartEnd.getBlock(j, i), "resetData did not reset properly");
            }
        }
    }

    @Test
    public void testPrepForGenerator() {
        mazeModelStartEnd.prepForGenerator();

        for (int i = 0; i < TEST_HEIGHT; i++) {
            for (int j = 0; j < TEST_WIDTH; j++) {
                if (i == 0 && j == TEST_START)
                    assertEquals(Block.START, mazeModelStartEnd.getBlock(j, i), "prepForGenerator did not prep start properly");
                else if (i == TEST_HEIGHT - 1 && j == TEST_END)
                    assertEquals(Block.END, mazeModelStartEnd.getBlock(j, i), "prepForGenerator did not prep end properly");
                else
                    assertEquals(Block.WALL, mazeModelStartEnd.getBlock(j, i), "prepForGenerator did not prep properly");
            }
        }
    }

    @Test
    public void testGetStartPosition() {
        Point point = new Point(TEST_START, 0);
        assertEquals(point, mazeModelStartEnd.getStartPosition(), "getStartPosition not implemented properly");
    }

    @Test
    public void testGetEndPosition() {
        Point point = new Point(TEST_END, TEST_HEIGHT - 1);
        assertEquals(point, mazeModelStartEnd.getEndPosition(), "getEndPosition not implemented properly");
    }

    @Test
    public void testGetStartX() {
        assertEquals(TEST_START, mazeModelStartEnd.getStartX(), "getStartX not implemented properly");
    }

    @Test
    public void testGetENDX() {
        assertEquals(TEST_END, mazeModelStartEnd.getEndX(), "getEndX not implemented properly");
    }


    @Test
    public void testSetBlock() {
        mazeModel.setBlock(Block.WALL, 0, 0);

        assertEquals(Block.WALL, mazeModel.getBlock(0, 0));
    }

    @Test
    public void testToString(){
        
        for (int col = 0; col < TEST_WIDTH; col++) {
            mazeModel.setBlock(Block.WALL, col, 0);
        }

        String expected = "";
        for (int i = 0; i < TEST_HEIGHT; i++) {
            for (int j = 0; j < TEST_WIDTH; j++) {
                if(i == 0)
                    expected += "W";
                else
                    expected += "B";
                expected += " ";
            }
            expected += "\n";
        }

        String actual = mazeModel.toString();

        assertEquals(expected, actual, "toString implemented incorrectly");
    }

}
