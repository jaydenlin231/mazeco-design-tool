package com.mazeco.utilities;

import com.mazeco.models.MazeModel;
import com.mazeco.models.Block;

import java.util.ArrayList;
import java.util.Collections;

public final class MazeGenerator {
    public static MazeModel generateMaze(int width, int height, int start, int end, String logo, String startImage, String endImage) {
        MazeModel mazeModel = new MazeModel(width, height, start, end, logo, startImage, endImage);
        mazeModel.prepForGenerator();
        mazeModel.setBlock(Block.LOGO, 3, 3);
        mazeModel.setBlock(Block.LOGO, 4, 3);
        mazeModel.setBlock(Block.LOGO, 3, 4);
        mazeModel.setBlock(Block.LOGO, 4, 4);
        mazeModel = DFS(mazeModel, 1, start);
        return mazeModel;
    }

    private static MazeModel DFS(MazeModel mazeModel, int row, int col) {
        Integer[] randomDirs = generateRandomDirs();
        for (int i = 0; i < randomDirs.length; i++) {
            switch (randomDirs[i]) {
                case 1 -> { //Up
                    if (row - 1 == 0 || mazeModel.getBlock(col, row - 2).equals(Block.START)) {
                        continue;
                    }
                    if (mazeModel.getBlock(col, row - 2).equals(Block.LOGO)) {
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
                    if (mazeModel.getBlock(col + 2, row).equals(Block.LOGO)) {
                        continue;
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
                    if (mazeModel.getBlock(col, row + 2).equals(Block.LOGO)) {
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
                    if (mazeModel.getBlock(col - 2, row).equals(Block.LOGO)) {
                        continue;
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

    private static Integer[] generateRandomDirs() {
        ArrayList<Integer> rand = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++) {
            rand.add(i + 1);
        }
        Collections.shuffle(rand);
        return rand.toArray(new Integer[4]);
    }
}
