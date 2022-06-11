package com.mazeco;

import static org.junit.jupiter.api.Assertions.*;

import com.mazeco.exception.UnsolvableMazeException;
import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;
import com.mazeco.utilities.MazeProblem;
import com.mazeco.utilities.MazeSolver;
import com.mazeco.utilities.MazeGenerator;
import org.junit.jupiter.api.*;

public class MazeGeneratorTest {
    private static final int TEST_WIDTH = 20;
    private static final int TEST_HEIGHT = 20;
    private static final int TEST_START = 1;
    private static final int TEST_END = 17;

    MazeModel mazeModel;

    @BeforeEach
    public void constructMazeGenerator() {
        mazeModel = MazeGenerator.generateMaze(TEST_WIDTH, TEST_HEIGHT, TEST_START, TEST_END, null, null ,null);
    }

    @Test
    public void testDimensions() {
        assertAll(() -> {
            assertEquals(TEST_WIDTH, mazeModel.getWidth(), "Width of the maze has been changed");
            assertEquals(TEST_HEIGHT, mazeModel.getHeight(), "Height of the maze has been changed");
        });
    }

    @Test
    public void testStartAndEndPointsStillIntact() {
        for (int i = 0; i < TEST_WIDTH; i++) {
            for (int j = 0; j < TEST_HEIGHT; j++) {
                if (j == 0 && i == TEST_START)
                    assertEquals(Block.START, mazeModel.getBlock(i, j), "Start point has been moved");
                else if (j == TEST_HEIGHT - 1 && i == TEST_END)
                    assertEquals(Block.END, mazeModel.getBlock(i, j), "End point has been moved");
            }
        }
    }

    @Test
    public void testForCorrectWalls() {
        for (int i = 0; i < TEST_WIDTH; i++) {
            for (int j = 0; j < TEST_HEIGHT; j++) {
                if (j == 0 && !(i == TEST_START))
                    assertEquals(Block.WALL, mazeModel.getBlock(i, j), "Top wall is missing or incomplete.");
                if (i == 0)
                    assertEquals(Block.WALL, mazeModel.getBlock(i, j), "Left wall is missing or incomplete.");
                if (i == TEST_WIDTH - 1)
                    assertEquals(Block.WALL, mazeModel.getBlock(i, j), "Right wall is missing or incomplete.");
                if (j == TEST_HEIGHT - 1 && !(i == TEST_END))
                    assertEquals(Block.WALL, mazeModel.getBlock(i, j), "Bottom wall is missing or incomplete.");
            }
        }
    }

    @Test
    public void testSolvable() {
        int numberOfTests = 100;

        for (int i = 0; i <= numberOfTests; i++) {
            constructMazeGenerator();
            try {
                MazeSolver.aStarGraphSearch(new MazeProblem(mazeModel));
            } catch (UnsolvableMazeException e) {
                fail("Generated maze not solvable");
            }
        }
    }


}
