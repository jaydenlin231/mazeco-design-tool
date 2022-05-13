package com.mazeco;

import static org.junit.jupiter.api.Assertions.*;

import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;

import org.junit.jupiter.api.*;


public class MazeModelTest {
    private static final int TEST_WIDTH = 10;
    private static final int TEST_HEIGHT = 10;

    private static final int MIN_WIDTH = 10;
    private static final int MIN_HEIGHT = 10;
    private static final int MAX_WIDTH = 100;
    private static final int MAX_HEIGHT = 100;

    MazeModel mazeModel;

    @BeforeEach
    public void constructMazeModel(){
        mazeModel = new MazeModel(TEST_WIDTH, TEST_HEIGHT);
    }
    
    @Test 
    public void contructorShouldThrowIllegalArgumentException(){
        assertAll(() -> {
            assertThrows(IllegalArgumentException.class, () ->{
                mazeModel = new MazeModel(MIN_WIDTH - 1, MIN_HEIGHT - 1);
            });

            assertThrows(IllegalArgumentException.class, () ->{
                mazeModel = new MazeModel(MAX_WIDTH + 1, MAX_HEIGHT + 1);
            });
        });

    }

    @Test 
    public void testGetWidth(){
        assertEquals(TEST_WIDTH, mazeModel.getWidth(), "getWidth not implemented correctly");
    }

    @Test 
    public void testGetHeight(){
        assertEquals(TEST_HEIGHT, mazeModel.getHeight(), "getHeight not implemented correctly");
    }

    @Test 
    public void testSetBlock(){
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
