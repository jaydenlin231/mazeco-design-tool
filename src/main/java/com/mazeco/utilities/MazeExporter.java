package com.mazeco.utilities;

import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MazeExporter {
    final MazeModel mazeModel;
    final int mazeWidth, mazeHeight;
    int width, height;
    int cell;
    BufferedImage image;

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
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        int x = 0;
        int y = 0;
        g.fillRect(0, 0, width - 1, height - 1);
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
                    g.setColor(Color.BLUE);
                    g.fillRect(x, y, cell, cell);
                    y += cell;
                }

            }
            x += cell;
            y = 0;
        }
    }

    public void ExportPNG(String path) throws IOException {
        ImageIO.write(image, "png", new File(path));
    }

    public BufferedImage getBufferedImage() {
        return image;
    }

    public ImageIcon getImageIcon(){
        return new ImageIcon(image);
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
