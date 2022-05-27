package com.mazeco.utilities;

import com.mazeco.models.MazeModel;
import com.mazeco.models.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MazeGenerator {
    private MazeModel maze;
    private int width;
    private int height;
    private int start;
    private int end;

    public MazeGenerator(int width, int height, int start, int end) {
        this.width = width;
        this.height = height;
        this.start = start;
        this.end = end;
        initialiseMaze();
        generateMaze();
    }

    public MazeModel initialiseMaze() {
        maze = new MazeModel(width, height);

        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                maze.setBlock(Block.WALL, j, i);
//                if(i == 0 || i == maze.getHeight() -1){
//                    maze.setBlock(Block.WALL, j, i);
//                }
                if (i == 1 && j == start) {
                    maze.setBlock(Block.START, j, i);
                    maze.setBlock(Block.START, j, i - 1);
                }
                if (i == maze.getHeight() - 1 && j == end) {
                    maze.setBlock(Block.END, j, i);
                }
//                if(j == 0 || j == maze.getWidth() -1){
//                    maze.setBlock(Block.WALL, j, i);
//                }

            }
        }
        return maze;
    }

    public void generateMaze() {
//        System.out.println(maze.toString());
        DFS(1, start);
        System.out.println(maze.toString());
    }

    public void DFS(int row, int col) {
        Integer[] randomDirs = generateRandomDirs();
        for (int i = 0; i < randomDirs.length; i++) {
            switch (randomDirs[i]) {
                case 1: //Up
                    if (row - 1 == 0 || maze.getBlock(col, row - 2).equals(Block.START)) {
                        continue;
                    }
                    if (!maze.getBlock(col, row - 2).equals(Block.BLANK)) {
                        maze.setBlock(Block.BLANK, col, row - 1);
                        if (!(row - 2 == maze.getHeight() - 1)) {
                            maze.setBlock(Block.BLANK, col, row - 2);
//                            System.out.println("Up");
//                            System.out.println(maze.toString());
                            DFS(row - 2, col);
                        }

                    }
                    break;
                case 2: //Right
                    if (col + 1 == maze.getWidth() - 1 || maze.getBlock(col + 2, row).equals(Block.START)) {
                        continue;
                    }
                    if (!maze.getBlock(col + 2, row).equals(Block.BLANK)) {
                        if (maze.getBlock(col + 1, row + 1).equals(Block.END)) {
                            maze.setBlock(Block.END, col + 1, row);
                        } else {
                            maze.setBlock(Block.BLANK, col + 1, row);
                        }
                        if (!(col + 2 == maze.getWidth() - 1)) {
                            maze.setBlock(Block.BLANK, col + 2, row);
//                            System.out.println("Right");
//                            System.out.println(maze.toString());
                            DFS(row, col + 2);
                        }
                    }
                    break;
                case 3: //Down
                    if (row + 1 == maze.getHeight() - 1 || maze.getBlock(col, row + 2).equals(Block.START)) {
                        continue;
                    }
                    if (!maze.getBlock(col, row + 2).equals(Block.BLANK)) {
                        if (maze.getBlock(col, row + 2).equals(Block.END)) {
                            maze.setBlock(Block.END, col, row + 1);
                        } else {
                            maze.setBlock(Block.BLANK, col, row + 1);
                        }

                        if (!(row + 2 == maze.getHeight() - 1)) {
                            maze.setBlock(Block.BLANK, col, row + 2);
//                            System.out.println("Down");
//                            System.out.println(maze.toString());
                            DFS(row + 2, col);
                        }
                    }
                    break;
                case 4: //Left
                    if (col - 1 == 0 || maze.getBlock(col - 2, row).equals(Block.START)) {
                        continue;
                    }
                    if (!maze.getBlock(col - 2, row).equals(Block.BLANK)) {
                        if (maze.getBlock(col - 1, row + 1).equals(Block.END)) {
                            maze.setBlock(Block.END, col - 1, row);
                        } else {
                            maze.setBlock(Block.BLANK, col - 1, row);
                        }
                        if (!(col - 2 == maze.getWidth() - 1)) {
                            maze.setBlock(Block.BLANK, col - 2, row);
//                            System.out.println("Left");
//                            System.out.println(maze.toString());
                            DFS(row, col - 2);
                        }
                    }
                    break;
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


    public static void main(String[] args) {
        MazeGenerator maze = new MazeGenerator(10, 10, 3, 4);

    }
}
