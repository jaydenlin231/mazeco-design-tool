package com.mazeco;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.*;

import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;
import com.mazeco.utilities.MazeProblem;


public class MazeProblemTest {
    private static final int TEST_WIDTH = 10;
    private static final int TEST_HEIGHT = 10;
    private static final int TEST_START = 1;
    private static final int TEST_END = 7;


    MazeModel testMazeModel;
    MazeProblem testMazeProblem;

    @BeforeEach
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
    public void constructMazeModel(){
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
        testMazeProblem = new MazeProblem(testMazeModel);
    }

    @Test
    /***
     *  Current state is at O
     * 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W B W B W B W B W W 
     *  S O B B B B B B B E 
     *  W B W B W B W B W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     * 
     */
    public void testActionsCanGoAllDirections(){
        ArrayList<String> expected = new ArrayList<String>(Arrays.asList(new String[]{"Up", "Down", "Left", "Right"}));
        ArrayList<String> actual = testMazeProblem.actions(new Point(1, 5));
        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected, actual, "actions not implemented correctly");
    }

    @Test
    /***
     *  Current state is at O
     * 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W B W B W B W B W W 
     *  S B O B B B B B B E 
     *  W B W B W B W B W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     * 
     */
    public void testActionsCanSomeDirections(){
        ArrayList<String> expected = new ArrayList<String>(Arrays.asList(new String[]{"Left", "Right"}));
        ArrayList<String> actual = testMazeProblem.actions(new Point(2, 5));
        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected, actual, "actions not implemented correctly");
    }

    @Test
    /***
     *  Current state is at O
     * 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W O W B W B W B W W 
     *  S B B B B B B B B E 
     *  W B W B W B W B W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     * 
     */
    public void testActionsDeadEndCanGoOneDirection(){
        ArrayList<String> expected = new ArrayList<String>(Arrays.asList(new String[]{"Down"}));
        ArrayList<String> actual = testMazeProblem.actions(new Point(1, 4));
        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected, actual, "actions not implemented correctly");
    }

    @Test
    /***
     *  Current state is at O
     * 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W O W B W B W B W W 
     *  S B B B B B B B B E 
     *  W B W B W B W B W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     * 
     */
    public void testResultGoingDown(){
        Point currentState = new Point(1, 4);
        Point expected = new Point(1, 5);
        Point actual = testMazeProblem.result(currentState, "Down");
        assertEquals(expected, actual, "actions not implemented correctly");
    }

    @Test
    /***
     *  Current state is at O
     * 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W B W B W B W B W W 
     *  S O B B B B B B B E 
     *  W B W B W B W B W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     * 
     */
    public void testResultGoingUp(){
        Point currentState = new Point(1, 5);
        Point expected = new Point(1, 4);
        Point actual = testMazeProblem.result(currentState, "Up");
        assertEquals(expected, actual, "actions not implemented correctly");
    }

    @Test
    /***
     *  Current state is at O
     * 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W B W B W B W B W W 
     *  S O B B B B B B B E 
     *  W B W B W B W B W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     * 
     */
    public void testResultGoingRight(){
        Point currentState = new Point(1, 5);
        Point expected = new Point(2, 5);
        Point actual = testMazeProblem.result(currentState, "Right");
        assertEquals(expected, actual, "actions not implemented correctly");
    }

    @Test
    /***
     *  Current state is at O
     * 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W B W B W B W B W W 
     *  S B O B B B B B B E 
     *  W B W B W B W B W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     *  W W W W W W W W W W 
     * 
     */
    public void testResultGoingLeft(){
        Point currentState = new Point(2, 5);
        Point expected = new Point(1, 5);
        Point actual = testMazeProblem.result(currentState, "Left");
        assertEquals(expected, actual, "actions not implemented correctly");
    }

    @Test
    public void testGoalTestNotAtGoal(){
        boolean expected = false;
        boolean actual = testMazeProblem.goalTest(new Point(8, 5));
        assertEquals(expected, actual, "goalTest not implemented correctly");
    }

    @Test
    public void testGoalTestAtGoal(){
        boolean expected = true;
        boolean actual = testMazeProblem.goalTest(new Point(9, 5));
        assertEquals(expected, actual, "goalTest not implemented correctly");
    }

    @Test
    public void testPathCost(){
        int expected = 1;
        int actual = testMazeProblem.pathCost(0);
        assertEquals(expected, actual, "pathCost not implemented correctly");
    }

}
