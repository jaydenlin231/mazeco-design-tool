package com.mazeco.utilities;

import com.mazeco.models.MazeModel;
import com.mazeco.models.Block;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Automatically generates a solvable maze through using Depth First Search algorithm
 */
public final class MazeGenerator {

    /**
     * Constructor to create an automatically generated and solvable MazeModel with declared start and finish.
     *
     * @param width  Width of the maze in number of cells.
     * @param height Height of the maze in number of cells.
     * @param start  Start column index of the maze in the top row.
     * @param end    End column index of the maze in the bottom row.
     * @return Returns MazeModel that has been automatically generated through Depth First Search algorithm.
     */
    public static MazeModel generateMaze(int width, int height, int start, int end) {
        MazeModel mazeModel = new MazeModel(width, height, start, end);
        mazeModel.prepForGenerator();
        mazeModel = DFS(mazeModel, 1, start);
        return mazeModel;
    }

    /**
     * Depth First Search algorithm to create a solvable maze starting from the given row and col.
     *
     * @param mazeModel Requires a MazeModel that has been prepared for the Generator where all the
     *                  cells except from the Start and End are Walls.
     * @param row       Starting cell row.
     * @param col       Starting cell column.
     * @return Returns a automatically generated mazeModel that is solvable.
     */
    private static MazeModel DFS(MazeModel mazeModel, int row, int col) {
        Integer[] randomDirs = generateRandomDirs();
        for (int i = 0; i < randomDirs.length; i++) {
            switch (randomDirs[i]) {
                case 1 -> { //Up
                    if (row - 1 == 0 || mazeModel.getBlock(col, row - 2).equals(Block.START)) {
                        continue;
                    }
                    if (!mazeModel.getBlock(col, row - 2).equals(Block.BLANK)) {
                        mazeModel.setBlock(Block.BLANK, col, row - 1);
                        if (!(row - 2 == mazeModel.getHeight() - 1)) {
                            mazeModel.setBlock(Block.BLANK, col, row - 2);
                            mazeModel = DFS(mazeModel, row - 2, col);
                        }

                    }
                }
                case 2 -> { //Right
                    if (col + 1 == mazeModel.getWidth() - 1 || mazeModel.getBlock(col + 2, row).equals(Block.START)) {
                        continue;
                    }
                    if (mazeModel.getBlock(col + 1, row + 1).equals(Block.END)) {
                        mazeModel.setBlock(Block.BLANK, col + 1, row);
                    }
                    if (!mazeModel.getBlock(col + 2, row).equals(Block.BLANK)) {
                        mazeModel.setBlock(Block.BLANK, col + 1, row);
                        if (!(col + 2 == mazeModel.getWidth() - 1)) {
                            mazeModel.setBlock(Block.BLANK, col + 2, row);
                            mazeModel = DFS(mazeModel, row, col + 2);
                        }
                    }
                }
                case 3 -> { //Down
                    if (row + 1 == mazeModel.getHeight() - 1 || mazeModel.getBlock(col, row + 2).equals(Block.START)) {
                        continue;
                    }
                    if (!mazeModel.getBlock(col, row + 2).equals(Block.BLANK)) {
                        mazeModel.setBlock(Block.BLANK, col, row + 1);
                        if (!(row + 2 == mazeModel.getHeight() - 1)) {
                            mazeModel.setBlock(Block.BLANK, col, row + 2);
                            mazeModel = DFS(mazeModel, row + 2, col);
                        }
                    }
                }
                case 4 -> { //Left
                    if (col - 1 == 0 || mazeModel.getBlock(col - 2, row).equals(Block.START)) {
                        continue;
                    }
                    if (mazeModel.getBlock(col - 1, row + 1).equals(Block.END)) {
                        mazeModel.setBlock(Block.BLANK, col - 1, row);
                    }
                    if (!mazeModel.getBlock(col - 2, row).equals(Block.BLANK)) {
                        mazeModel.setBlock(Block.BLANK, col - 1, row);
                        if (!(col - 2 == mazeModel.getWidth() - 1)) {
                            mazeModel.setBlock(Block.BLANK, col - 2, row);
                            mazeModel = DFS(mazeModel, row, col - 2);
                        }
                    }
                }
            }
        }
        return mazeModel;
    }

    /**
     * Generates an array of random integers from 1-4 to randomise the traversal of DFS.
     *
     * @return Returns an Integer array of 4 elements where numbers from 1-4 are shuffled.
     */
    private static Integer[] generateRandomDirs() {
        ArrayList<Integer> rand = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            rand.add(i + 1);
        }
        Collections.shuffle(rand);
        return rand.toArray(new Integer[4]);
    }
}
