package com.mazeco;

import java.awt.Point;

import com.mazeco.exception.UnsolvableMazeException;
import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;
import com.mazeco.utilities.MazeProblem;
import com.mazeco.utilities.MazeSolver;
import com.mazeco.utilities.Node;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class MazeSolverTest {
    private static final int TEST_WIDTH = 10;
    private static final int TEST_HEIGHT = 10;
    private static final int TEST_START = 1;
    private static final int TEST_END = 7;
    
    MazeModel testMazeModel;
    MazeProblem testMazeProblem;
    MazeModel testMazeModel2;
    MazeProblem testMazeProblem2;
    MazeModel unsolvableMazeModel;
    MazeProblem unsolvableMazeProblem;

    @BeforeEach
    public void constructMazeModel(){

        constructTestSolveMazeModel();
        constructTestSolveMazeModel2();
        constructUnsolvableMazeModel();
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
        testMazeProblem = new MazeProblem(testMazeModel);
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

        testMazeProblem2 = new MazeProblem(testMazeModel2);
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
        unsolvableMazeProblem = new MazeProblem(unsolvableMazeModel);
    }

    @Test
    public void testAStartGraphSearch() throws UnsolvableMazeException{
        Node expected = new Node(new Point(9, 5));
        Node actual = MazeSolver.aStarGraphSearch(testMazeProblem);
    }

    @Test
    public void testAStartGraphSearch2() throws UnsolvableMazeException{
        Node expected = new Node(new Point(9, 5));
        Node actual = MazeSolver.aStarGraphSearch(testMazeProblem2);
    }

    @Test
    public void testAStartGraphSearchShouldThrowUnsolvableMazeException() throws UnsolvableMazeException{
        assertThrows(UnsolvableMazeException.class, () -> {
            Node actual = MazeSolver.aStarGraphSearch(unsolvableMazeProblem);
        });
    }
}
