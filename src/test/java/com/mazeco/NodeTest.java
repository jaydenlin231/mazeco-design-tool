package com.mazeco;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import com.mazeco.exception.UnsolvableMazeException;
import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;
import com.mazeco.utilities.MazeProblem;
import com.mazeco.utilities.MazeSolver;
import com.mazeco.utilities.Node;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class NodeTest {
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
    public void testGetState(){
        Node aNode = new Node(new Point(1,2));
        Point expected = new Point(1,2);
        Point actual = aNode.getState();

        assertEquals(expected, actual, "getState not implemented correctly");
    }

    @Test
    public void testGetPathCost(){
        Node aNode = new Node(new Point(1,2));
        int expected = 0;
        int actual = aNode.getPathCost();

        assertEquals(expected, actual, "getPathCost not implemented correctly");

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
    public void testExpandAllDirections(){
        Node currentNode = new Node(new Point(1, 5));

        Node expectedNode1 = new Node(new Point(0,5));
        Node expectedNode2 = new Node(new Point(2,5));
        Node expectedNode3 = new Node(new Point(1,4));
        Node expectedNode4 = new Node(new Point(1,6));
        ArrayList<Node> expected = new ArrayList<Node>(Arrays.asList(new Node[]{expectedNode1, expectedNode2, expectedNode3, expectedNode4}));
        ArrayList<Node> actual = currentNode.expand(testMazeProblem);
        assertTrue(expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(actual), "expand not implemented correctly");
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
    public void testExpandSomeDirections(){
        Node currentNode = new Node(new Point(2, 5));

        Node expectedNode1 = new Node(new Point(1,5));
        Node expectedNode2 = new Node(new Point(3,5));
        ArrayList<Node> expected = new ArrayList<Node>(Arrays.asList(new Node[]{expectedNode1, expectedNode2}));
        ArrayList<Node> actual = currentNode.expand(testMazeProblem);
        assertTrue(expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(actual), "expand not implemented correctly");
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
    public void testExpandOneDirectionDeadEnd(){
        Node currentNode = new Node(new Point(1, 4));

        Node expectedNode1 = new Node(new Point(1,5));
        ArrayList<Node> expected = new ArrayList<Node>(Arrays.asList(new Node[]{expectedNode1}));
        ArrayList<Node> actual = currentNode.expand(testMazeProblem);
        assertTrue(expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(actual), "expand not implemented correctly");
    }

    @Test
    public void testGetSolutionPath() throws UnsolvableMazeException{
        Node expectedNode0 = new Node(new Point(0,5));
        Node expectedNode1 = new Node(new Point(1,5));
        Node expectedNode2 = new Node(new Point(2,5));
        Node expectedNode3 = new Node(new Point(3,5));
        Node expectedNode4 = new Node(new Point(4,5));
        Node expectedNode5 = new Node(new Point(5,5));
        Node expectedNode6 = new Node(new Point(6,5));
        Node expectedNode7 = new Node(new Point(7,5));
        Node expectedNode8 = new Node(new Point(8,5));
        Node expectedNode9 = new Node(new Point(9,5));

        ArrayList<Node> expected = new ArrayList<Node>(Arrays.asList(new Node[]{expectedNode0,
                                                                                expectedNode1,
                                                                                expectedNode2,
                                                                                expectedNode3,
                                                                                expectedNode4,
                                                                                expectedNode5,
                                                                                expectedNode6,
                                                                                expectedNode7,
                                                                                expectedNode8,
                                                                                expectedNode9,
                                                                                }));

        ArrayList<Node> actual = MazeSolver.aStarGraphSearch(testMazeProblem).getSolutionPath();

        assertTrue(expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(actual), "getSolutionPath not implemented correctly");
    }

    @Test
    public void testGetSolutionPath2() throws UnsolvableMazeException{
        Node expectedNode0 = new Node(new Point(0,5));
        Node expectedNode1 = new Node(new Point(1,5));
        Node expectedNode2 = new Node(new Point(2,5));
        Node expectedNode3 = new Node(new Point(3,5));
        Node expectedNode4 = new Node(new Point(4,5));
        Node expectedNode5 = new Node(new Point(5,5));
        Node expectedNode6 = new Node(new Point(6,5));
        Node expectedNode7 = new Node(new Point(7,5));
        Node expectedNode8 = new Node(new Point(8,5));
        Node expectedNode9 = new Node(new Point(9,5));

        ArrayList<Node> expected = new ArrayList<Node>(Arrays.asList(new Node[]{expectedNode0,
                                                                                expectedNode1,
                                                                                expectedNode2,
                                                                                expectedNode3,
                                                                                expectedNode4,
                                                                                expectedNode5,
                                                                                expectedNode6,
                                                                                expectedNode7,
                                                                                expectedNode8,
                                                                                expectedNode9,
                                                                                }));

        ArrayList<Node> actual = MazeSolver.aStarGraphSearch(testMazeProblem2).getSolutionPath();

        assertTrue(expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(actual), "getSolutionPath not implemented correctly");
    }

    @Test
    public void testGetSolutionPathShouldThrowUnsolvableMazeException() throws UnsolvableMazeException{
        Node expectedNode1 = new Node(new Point(1,5));
        Node expectedNode2 = new Node(new Point(2,5));
        Node expectedNode3 = new Node(new Point(3,5));
        Node expectedNode4 = new Node(new Point(4,5));
        Node expectedNode5 = new Node(new Point(5,5));
        Node expectedNode6 = new Node(new Point(6,5));
        Node expectedNode7 = new Node(new Point(7,5));
        Node expectedNode8 = new Node(new Point(8,5));
        Node expectedNode9 = new Node(new Point(9,5));

        ArrayList<Node> expected = new ArrayList<Node>(Arrays.asList(new Node[]{expectedNode1,
                                                                                expectedNode2,
                                                                                expectedNode3,
                                                                                expectedNode4,
                                                                                expectedNode5,
                                                                                expectedNode6,
                                                                                expectedNode7,
                                                                                expectedNode8,
                                                                                expectedNode9,
                                                                                }));
        assertThrows(UnsolvableMazeException.class, () -> {
            ArrayList<Node> actual = MazeSolver.aStarGraphSearch(unsolvableMazeProblem).getSolutionPath();
        });

    }

    @Test
    public void testGetSolutionPoints() throws UnsolvableMazeException{
        Point expectedNode1 = new Point(1,5);
        Point expectedNode2 = new Point(2,5);
        Point expectedNode3 = new Point(3,5);
        Point expectedNode4 = new Point(4,5);
        Point expectedNode5 = new Point(5,5);
        Point expectedNode6 = new Point(6,5);
        Point expectedNode7 = new Point(7,5);
        Point expectedNode8 = new Point(8,5);
        Point expectedNode9 = new Point(9,5);

        ArrayList<Point> expected = new ArrayList<Point>(Arrays.asList(new Point[]{expectedNode1,
                                                                                   expectedNode2,
                                                                                   expectedNode3,
                                                                                   expectedNode4,
                                                                                   expectedNode5,
                                                                                   expectedNode6,
                                                                                   expectedNode7,
                                                                                   expectedNode8,
                                                                                   expectedNode9,
                                                                                   }));

        ArrayList<Point> actual = MazeSolver.aStarGraphSearch(testMazeProblem).getSolutionPoints();
        
        assertTrue(expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(actual), "getSolutionPoints not implemented correctly");
    }

    @Test
    public void testGetSolutionPoints2() throws UnsolvableMazeException{
        Point expectedNode1 = new Point(1,5);
        Point expectedNode2 = new Point(2,5);
        Point expectedNode3 = new Point(3,5);
        Point expectedNode4 = new Point(4,5);
        Point expectedNode5 = new Point(5,5);
        Point expectedNode6 = new Point(6,5);
        Point expectedNode7 = new Point(7,5);
        Point expectedNode8 = new Point(8,5);
        Point expectedNode9 = new Point(9,5);

        ArrayList<Point> expected = new ArrayList<Point>(Arrays.asList(new Point[]{expectedNode1,
                                                                                   expectedNode2,
                                                                                   expectedNode3,
                                                                                   expectedNode4,
                                                                                   expectedNode5,
                                                                                   expectedNode6,
                                                                                   expectedNode7,
                                                                                   expectedNode8,
                                                                                   expectedNode9,
                                                                                   }));

        ArrayList<Point> actual = MazeSolver.aStarGraphSearch(testMazeProblem2).getSolutionPoints();

        assertTrue(expected.size() == actual.size() && expected.containsAll(actual) && actual.containsAll(actual), "getSolutionPoints not implemented correctly");
    }

    @Test
    public void testGetSolutionPointsShouldThrowUnsolvableMazeException() throws UnsolvableMazeException{
        Point expectedNode0 = new Point(0,5);
        Point expectedNode1 = new Point(1,5);
        Point expectedNode2 = new Point(2,5);
        Point expectedNode3 = new Point(3,5);
        Point expectedNode4 = new Point(4,5);
        Point expectedNode5 = new Point(5,5);
        Point expectedNode6 = new Point(6,5);
        Point expectedNode7 = new Point(7,5);
        Point expectedNode8 = new Point(8,5);
        Point expectedNode9 = new Point(9,5);

        ArrayList<Point> expected = new ArrayList<Point>(Arrays.asList(new Point[]{expectedNode0,
                                                                                   expectedNode1,
                                                                                   expectedNode2,
                                                                                   expectedNode3,
                                                                                   expectedNode4,
                                                                                   expectedNode5,
                                                                                   expectedNode6,
                                                                                   expectedNode7,
                                                                                   expectedNode8,
                                                                                   expectedNode9,
                                                                                   }));
        assertThrows(UnsolvableMazeException.class, () -> {
            ArrayList<Point> actual = MazeSolver.aStarGraphSearch(unsolvableMazeProblem).getSolutionPoints();
        });
    }

    @Test
    public void testEqualsTrue(){
        Node aNode = new Node(new Point(0, 0));
        Node anotherNode = new Node(new Point(0, 0));

        assertEquals(true, aNode.equals(anotherNode));
    }

    @Test
    public void testEqualsFalse(){
        Node aNode = new Node(new Point(0, 0));
        Node anotherNode = new Node(new Point(0, 1));

        assertEquals(false, aNode == anotherNode);
    }

}
