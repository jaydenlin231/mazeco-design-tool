package com.mazeco;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import com.mazeco.exception.UnsolvableMazeException;
import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;


import java.awt.Point;


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
    
    MazeModel testMazeModel;
    MazeModel testMazeModel2;
    MazeModel unsolvableMazeModel;

    @BeforeEach
    public void constructMazeModel(){
        mazeModel = new MazeModel(TEST_WIDTH, TEST_HEIGHT);
        mazeModelStartEnd = new MazeModel(TEST_WIDTH, TEST_HEIGHT, TEST_START, TEST_END);
        unsolvableMazeModel = new MazeModel(TEST_WIDTH, TEST_HEIGHT, TEST_START, TEST_END);

        constructTestSolveMazeModel();
        constructTestSolveMazeModel2();
        constructUnsolvableMazeModel();
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
    public void testGetBlockShouldThrowIndexOutOfBoundsExceptionWhenExceedMax() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            mazeModel.getBlock(TEST_WIDTH + 1, TEST_HEIGHT + 1);
        });
    }

    @Test
    public void testGetBlockShouldThrowIndexOutOfBoundsExceptionWhenNegativeIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            mazeModel.getBlock(-1, -1);
        });
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
    public void testSetBlockShouldThrowIndexOutOfBoundsExceptionWhenExceedMax() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            mazeModel.setBlock(Block.BLANK, TEST_WIDTH + 1, TEST_HEIGHT + 1);
        });
    }

    @Test
    public void testSetBlockShouldThrowIndexOutOfBoundsExceptionWhenNegativeIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            mazeModel.setBlock(Block.BLANK, -1, -1);
        });
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

    @Test
    public void testSetLogoAreaOneBlockSquare(){
        mazeModel.setLogoArea(new Point(1,1), new Point(1,1));
            assertEquals(Block.LOGO, mazeModel.getBlock(1, 1), "setLogoArea implemented incorrectly");
    }

    @Test
    public void testSetLogoAreaSquare(){
        mazeModel.setLogoArea(new Point(1,1), new Point(TEST_WIDTH - 1,TEST_HEIGHT - 1));
        
        for (int row = 1; row < TEST_HEIGHT - 1; row++) {
            for (int col = 1; col < TEST_WIDTH - 1; col++) {
                assertEquals(Block.LOGO, mazeModel.getBlock(col, row), "setLogoArea implemented incorrectly");
            }
        }
    }

    @Test
    public void testSetLogoAreaSquareReverse(){
        mazeModel.setLogoArea(new Point(TEST_WIDTH - 1,TEST_HEIGHT - 1), new Point(1,1));
        
        for (int row = 1; row < TEST_HEIGHT - 1; row++) {
            for (int col = 1; col < TEST_WIDTH - 1; col++) {
                assertEquals(Block.LOGO, mazeModel.getBlock(col, row), "setLogoArea implemented incorrectly");
            }
        }
    }

    @Test
    public void testSetLogoAreaRectangleHorizontal(){
        mazeModel.setLogoArea(new Point(1,1), new Point(TEST_WIDTH - 1,(TEST_HEIGHT - 1) / 2));
        
        for (int row = 1; row < (TEST_HEIGHT - 1) / 2; row++) {
            for (int col = 1; col < TEST_WIDTH - 1; col++) {
                assertEquals(Block.LOGO, mazeModel.getBlock(col, row), "setLogoArea implemented incorrectly");
            }
        }
    }

    @Test
    public void testSetLogoAreaRectangleHorizontalReverse(){
        mazeModel.setLogoArea(new Point(TEST_WIDTH - 1,(TEST_HEIGHT - 1) / 2), new Point(1,1));
        
        for (int row = 1; row < (TEST_HEIGHT - 1) / 2; row++) {
            for (int col = 1; col < TEST_WIDTH - 1; col++) {
                assertEquals(Block.LOGO, mazeModel.getBlock(col, row), "setLogoArea implemented incorrectly");
            }
        }
    }

    @Test
    public void testSetLogoAreaRectangleVertical(){
        mazeModel.setLogoArea(new Point(1,1), new Point((TEST_WIDTH - 1) / 2,TEST_HEIGHT - 1));
        
        for (int row = 1; row < TEST_HEIGHT - 1; row++) {
            for (int col = 1; col < (TEST_WIDTH - 1) / 2; col++) {
                assertEquals(Block.LOGO, mazeModel.getBlock(col, row), "setLogoArea implemented incorrectly");
            }
        }
    }

    @Test
    public void testSetLogoAreaRectangleVerticalReverse(){
        mazeModel.setLogoArea(new Point((TEST_WIDTH - 1) / 2,TEST_HEIGHT - 1), new Point(1,1));
        
        for (int row = 1; row < TEST_HEIGHT - 1; row++) {
            for (int col = 1; col < (TEST_WIDTH - 1) / 2; col++) {
                assertEquals(Block.LOGO, mazeModel.getBlock(col, row), "setLogoArea implemented incorrectly");
            }
        }
    }

    @Test
    public void testRemoveLogoOneBlockSquare(){
        mazeModel.setLogoArea(new Point(1,1), new Point(1,1));
        mazeModel.removeLogo();
        assertEquals(Block.WALL, mazeModel.getBlock(1, 1), "removeLogo implemented incorrectly");
    }

    @Test
    public void testRemoveLogoSquare(){
        mazeModel.setLogoArea(new Point(1,1), new Point(TEST_WIDTH - 1,TEST_HEIGHT - 1));
        mazeModel.removeLogo();
        for (int row = 1; row < TEST_HEIGHT - 1; row++) {
            for (int col = 1; col < TEST_WIDTH - 1; col++) {
                assertEquals(Block.WALL, mazeModel.getBlock(col, row), "removeLogo implemented incorrectly");
            }
        }
    }

    @Test
    public void testRemoveLogoRectangleHorizontal(){
        
        mazeModel.setLogoArea(new Point(1,1), new Point(TEST_WIDTH - 1,(TEST_HEIGHT - 1) / 2));
        mazeModel.removeLogo();
        
        for (int row = 1; row < (TEST_HEIGHT - 1) / 2; row++) {
            for (int col = 1; col < TEST_WIDTH - 1; col++) {
                assertEquals(Block.WALL, mazeModel.getBlock(col, row), "removeLogo implemented incorrectly");
            }
        }
    }


    @Test
    public void testRemoveLogoRectangleVertical(){
        mazeModel.setLogoArea(new Point(1,1), new Point((TEST_WIDTH - 1) / 2,TEST_HEIGHT - 1));
        mazeModel.removeLogo();
        
        for (int row = 1; row < TEST_HEIGHT - 1; row++) {
            for (int col = 1; col < (TEST_WIDTH - 1) / 2; col++) {
                assertEquals(Block.WALL, mazeModel.getBlock(col, row), "removeLogo implemented incorrectly");
            }
        }
    }

    @Test
    public void testGetStartLogoPointOneBlockSquare(){
        Point startPoint = new Point(1,1);
        Point endPoint = new Point(1,1);
        mazeModel.setLogoArea(startPoint, endPoint);
        
        assertEquals(startPoint, mazeModel.getStartLogoPoint(), "getStartLogoPoint implemented incorrectly");
    }
    @Test
    public void testGetEndLogoPointOneBlockSquare(){
        Point startPoint = new Point(1,1);
        Point endPoint = new Point(1,1);
        mazeModel.setLogoArea(startPoint, endPoint);
        
        assertEquals(endPoint, mazeModel.getEndLogoPoint(), "getEngLogoPoint implemented incorrectly");
    }

    @Test
    public void testGetStartLogoPointSquare(){
        Point startPoint = new Point(1,1);
        Point endPoint = new Point(TEST_WIDTH - 1,TEST_HEIGHT - 1);
        mazeModel.setLogoArea(startPoint, endPoint);
        
        assertEquals(startPoint, mazeModel.getStartLogoPoint(), "getStartLogoPoint implemented incorrectly");
    }
    @Test
    public void testGetEndLogoPointSquare(){
        Point startPoint = new Point(1,1);
        Point endPoint = new Point(TEST_WIDTH - 1,TEST_HEIGHT - 1);

        mazeModel.setLogoArea(startPoint, endPoint);
        
        assertEquals(endPoint, mazeModel.getEndLogoPoint(), "getEngLogoPoint implemented incorrectly");
    }

    @Test
    public void testGetEndLogoPointRectangleHorizontal(){
        Point startPoint = new Point(1,1);
        Point endPoint =  new Point(TEST_WIDTH - 1,(TEST_HEIGHT - 1) / 2);
        
        mazeModel.setLogoArea(startPoint, endPoint);

        assertEquals(endPoint, mazeModel.getEndLogoPoint(), "getEngLogoPoint implemented incorrectly");
    }
    
    @Test
    public void testGetEndLogoPointRectangleVertical(){
        Point startPoint = new Point(1,1);
        Point endPoint =  new Point((TEST_WIDTH - 1) / 2,TEST_HEIGHT - 1);
        
        mazeModel.setLogoArea(startPoint, endPoint);

        assertEquals(endPoint, mazeModel.getEndLogoPoint(), "getEngLogoPoint implemented incorrectly");
    }

    /***
     *  Testing Maze 
     * 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W B W B W B W B W W 
     *  S B B B B B B B B E 
     *  W B W B W B W B W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     * 
     */
    private void constructTestSolveMazeModel(){
        testMazeModel = new MazeModel(TEST_WIDTH, TEST_HEIGHT, TEST_START, TEST_END);
        // reset start and end blocks
        testMazeModel.setBlock(Block.BLANK, TEST_START, 0);
        testMazeModel.setBlock(Block.BLANK, TEST_END, testMazeModel.getHeight() - 1);

        for (int row = 0; row < testMazeModel.getHeight(); row++) {
            for (int col = 0; col < testMazeModel.getWidth(); col++) {
                testMazeModel.setBlock(Block.WALL, col, row);
            }
        }
        for (int i = 0; i < testMazeModel.getWidth(); i++) {
            if(i == 0){
                testMazeModel.setBlock(Block.START, i, 5);
                continue;
            }
            if(i == testMazeModel.getWidth() - 1){
                testMazeModel.setBlock(Block.END, i, 5);
                continue;
            }
            if(i != 0 && i != testMazeModel.getWidth() - 1 && i % 2 == 1){
                testMazeModel.setBlock(Block.BLANK, i, 5 - 1);
                testMazeModel.setBlock(Block.BLANK, i, 5 + 1);
            }
            testMazeModel.setBlock(Block.BLANK, i, 5);
        }
    }

    /***
     *  Testing Maze 
     * 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W B B B B B B B B W
     *  W B W B W B W B W W 
     *  S B B B B B B B B E 
     *  W B W B W B W B W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     * 
     */
    private void constructTestSolveMazeModel2(){
        testMazeModel2 = new MazeModel(TEST_WIDTH, TEST_HEIGHT, TEST_START, TEST_END);
        // reset start and end blocks
        testMazeModel2.setBlock(Block.BLANK, TEST_START, 0);
        testMazeModel2.setBlock(Block.BLANK, TEST_END, testMazeModel2.getHeight() - 1);

        for (int row = 0; row < testMazeModel2.getHeight(); row++) {
            for (int col = 0; col < testMazeModel2.getWidth(); col++) {
                testMazeModel2.setBlock(Block.WALL, col, row);
            }
        }
        for (int i = 0; i < testMazeModel2.getWidth(); i++) {
            if(i == 0){
                testMazeModel2.setBlock(Block.START, i, 5);
                continue;
            }
            if(i == testMazeModel2.getWidth() - 1){
                testMazeModel2.setBlock(Block.END, i, 5);
                continue;
            }
            
            if(i != 0 && i != testMazeModel2.getWidth() - 1 && i % 2 == 1){
                testMazeModel2.setBlock(Block.BLANK, i, 5 - 1);
                testMazeModel2.setBlock(Block.BLANK, i, 5 + 1);
            }
            testMazeModel2.setBlock(Block.BLANK, i, 5);
        }
        for (int i = 0; i < testMazeModel2.getWidth() - 1; i++) {
            if(i != 0 && i != testMazeModel2.getWidth()){
                testMazeModel2.setBlock(Block.BLANK, i, 3);
                continue;
            }
        }
    }

    @Test
    public void testSolve() throws UnsolvableMazeException{
        testMazeModel.solve();

        for (int i = 1; i < testMazeModel.getWidth() - 1; i++) {
            assertEquals(Block.PATH, testMazeModel.getBlock(i, 5), "solve implemented incorrectly.");
        }
    }

    @Test
    public void testSolveShouldBeOptimalSolution() throws UnsolvableMazeException{
        testMazeModel2.solve();

        for (int i = 1; i < testMazeModel2.getWidth() - 1; i++) {
            assertEquals(Block.PATH, testMazeModel2.getBlock(i, 5), "solve implemented incorrectly.");
        }
    }

    /***
     *  Testing Maze 
     * 
     *  W S W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W E W W 
     * 
     */
    private void constructUnsolvableMazeModel() {
        unsolvableMazeModel = new MazeModel(TEST_WIDTH, TEST_HEIGHT, TEST_START, TEST_END);
        for (int row = 0; row < unsolvableMazeModel.getHeight(); row++) {
            inner:
            for (int col = 0; col < unsolvableMazeModel.getWidth(); col++) {
                if(unsolvableMazeModel.getBlock(col, row).equals(Block.START) || unsolvableMazeModel.getBlock(col, row).equals(Block.END))
                    continue inner;
                    unsolvableMazeModel.setBlock(Block.WALL, col, row);
            }
        }
    }

    @Test
    public void testSolveShouldThrowUnsolvableMazeException(){
        assertThrows(UnsolvableMazeException.class, () -> {
            unsolvableMazeModel.solve();
        });
        
    }


    @Test
    public void testGetSolutionPercentage1() throws UnsolvableMazeException{
        Double expected = 50.0;
        Double actual = testMazeModel.getSolutionPercentage();

        assertEquals(expected, actual, "getSolutionPercentage not implemented correctly");
    }

    @Test
    public void testGetSolutionPercentage2() throws UnsolvableMazeException{
        Double expected = 33.33;
        Double actual = testMazeModel2.getSolutionPercentage();
        assertEquals(expected, actual, "getSolutionPercentage not implemented correctly");
    }

    @Test
    public void testGetSolutionPercentageShouldThrowUnsolvableMazeException() throws UnsolvableMazeException{
        Double expected = 33.33;

        assertThrows(UnsolvableMazeException.class, () -> {
            Double actual = unsolvableMazeModel.getSolutionPercentage();
        });
    }

    @Test
    public void clearSolutionTest() throws UnsolvableMazeException{
        testMazeModel.solve();
        testMazeModel.clearSolution();
        for (int i = 1; i < testMazeModel.getWidth() - 1; i++) {
            assertEquals(Block.BLANK, testMazeModel.getBlock(i, 5), "solve implemented incorrectly.");
        }
    }

}
