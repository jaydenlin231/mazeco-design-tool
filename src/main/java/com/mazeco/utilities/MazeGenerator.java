package com.mazeco.utilities;

import com.mazeco.models.MazeModel;
import com.mazeco.models.Block;

import java.util.ArrayList;
import java.util.Collections;

public final class MazeGenerator {
    private MazeModel maze;
    private final int width;
    private final int height;
    private final int start;
    private final int end;

    public MazeGenerator(int width, int height, int start, int end) {
        this.width = width;
        this.height = height;
        this.start = start;
        this.end = end;
        initialiseMaze();
        generateMaze();
    }

    public void initialiseMaze() {
        maze = new MazeModel(width, height, start, end);
        maze.prepForGenerator();
    }

    public void generateMaze() {
        DFS(1, start);
//        System.out.println(maze.toString());
    }

    public void DFS(int row, int col) {
        Integer[] randomDirs = generateRandomDirs();
        for (int i = 0; i < randomDirs.length; i++) {
            switch (randomDirs[i]) {
                case 1 -> { //Up
                    if (row - 1 == 0 || maze.getBlock(col, row - 2).equals(Block.START)) {
                        continue;
                    }
                    if (!maze.getBlock(col, row - 2).equals(Block.BLANK)) {
                        maze.setBlock(Block.BLANK, col, row - 1);
                        if (!(row - 2 == maze.getHeight() - 1)) {
                            maze.setBlock(Block.BLANK, col, row - 2);
                            DFS(row - 2, col);
                        }

                    }
                }
                case 2 -> { //Right
                    if (col + 1 == maze.getWidth() - 1 || maze.getBlock(col + 2, row).equals(Block.START)) {
                        continue;
                    }
                    if (maze.getBlock(col + 1, row + 1).equals(Block.END)) {
                        maze.setBlock(Block.BLANK, col + 1, row);
                    }
                    if (!maze.getBlock(col + 2, row).equals(Block.BLANK)) {
                        maze.setBlock(Block.BLANK, col + 1, row);
                        if (!(col + 2 == maze.getWidth() - 1)) {
                            maze.setBlock(Block.BLANK, col + 2, row);
                            DFS(row, col + 2);
                        }
                    }
                }
                case 3 -> { //Down
                    if (row + 1 == maze.getHeight() - 1 || maze.getBlock(col, row + 2).equals(Block.START)) {
                        continue;
                    }
                    if (!maze.getBlock(col, row + 2).equals(Block.BLANK)) {
                        maze.setBlock(Block.BLANK, col, row + 1);
                        if (!(row + 2 == maze.getHeight() - 1)) {
                            maze.setBlock(Block.BLANK, col, row + 2);
                            DFS(row + 2, col);
                        }
                    }
                }
                case 4 -> { //Left
                    if (col - 1 == 0 || maze.getBlock(col - 2, row).equals(Block.START)) {
                        continue;
                    }
                    if (maze.getBlock(col - 1, row + 1).equals(Block.END)) {
                        maze.setBlock(Block.BLANK, col - 1, row);
                    }
                    if (!maze.getBlock(col - 2, row).equals(Block.BLANK)) {
                        maze.setBlock(Block.BLANK, col - 1, row);
                        if (!(col - 2 == maze.getWidth() - 1)) {
                            maze.setBlock(Block.BLANK, col - 2, row);
                            DFS(row, col - 2);
                        }
                    }
                }
            }
        }
    }

    public Integer[] generateRandomDirs() {
        ArrayList<Integer> rand = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            rand.add(i + 1);
        }
        Collections.shuffle(rand);
        return rand.toArray(new Integer[4]);
    }

    public MazeModel getMaze() {
        return maze;
    }


//    public static void main(String[] args) {
//        MazeGenerator maze = new MazeGenerator(10, 10, 3, 4);
//
//    }
}
