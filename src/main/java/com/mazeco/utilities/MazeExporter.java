package com.mazeco.utilities;

import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class MazeExporter {
    final MazeModel mazeModel;
    final int mazeWidth, mazeHeight;
    int width, height;
    int cell;
    BufferedImage imageClean;
    BufferedImage imageSolved;

    public MazeExporter(MazeModel mazeModel, int cellSize) {
        this.mazeModel = mazeModel;
        mazeWidth = mazeModel.getWidth();
        mazeHeight = mazeModel.getHeight();
        cell = cellSize;
        width = mazeWidth * cell;
        height = mazeHeight * cell;
        System.out.println("Cell size: " + cell + ",Width: " + width + ", Height: " + height);
        paint();
    }

    public void paint() {
        imageClean = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imageClean.createGraphics();
        int x = 0;
        int y = 0;
        g.fillRect(0, 0, width - 1, height - 1);
        g.setColor(Color.BLACK);
        g.drawRect(1, 1, width - 2, height - 2);
        for (int i = 0; i < mazeModel.getWidth(); i++) {
            for (int j = 0; j < mazeModel.getHeight(); j++) {
                if (mazeModel.getBlock(i, j).equals(Block.WALL)) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, cell, cell);
                    y += cell;
                }
                if (mazeModel.getBlock(i, j).equals(Block.BLANK)) {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, cell, cell);
                    y += cell;
                }
                if (mazeModel.getBlock(i, j).equals(Block.START)) {
                    g.setColor(Color.RED);
                    g.fillRect(x, y, cell, cell);
                    y += cell;
                }
                if (mazeModel.getBlock(i, j).equals(Block.END)) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x, y, cell, cell);
                    y += cell;
                }
                if (mazeModel.getBlock(i, j).equals(Block.PATH)) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(x, y, cell, cell);
                    y += cell;
                }

            }
            x += cell;
            y = 0;
        }
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
    }

    public void ExportPNG(File path, String name, String firstName, String lastName, String date, boolean withSolution) throws IOException {
        String solvedName = name + "Solved" + "_" + lastName + "_" + firstName + "_" + date + ".png";
        String fileNameClean = name + "_" + lastName + "_" + firstName + "_" + date + ".png";
        Path currentPath = Paths.get(String.valueOf(path));
        Path filePath = Paths.get(currentPath.toString(), fileNameClean);
        if (withSolution == true) {
            ImageIO.write(imageClean, "png", new File(String.valueOf(filePath)));
            mazeModel.solve();
            paint();
            filePath = Paths.get(currentPath.toString(), solvedName);
            ImageIO.write(imageClean, "png", new File(String.valueOf(filePath)));
            mazeModel.clearSolution();
        } else {
            ImageIO.write(imageClean, "png", new File(String.valueOf(filePath)));
        }
    }


    public ImageIcon getImageIcon() {
        return new ImageIcon(imageClean);
    }


//    // Testing
//    public static void main(String[] a) throws IOException {
//        int width = 100;
//        int height = 100;
//        int cellSize = 32;
//
//        MazeGenerator mazeGen = new MazeGenerator(width, height, 1, width - 3);
//        MazeModel maze = mazeGen.getMaze();
//        MazeExporter exporter = new MazeExporter(maze, cellSize);
//        exporter.ExportPNG("./Mazes/test.png");
//    }
}
