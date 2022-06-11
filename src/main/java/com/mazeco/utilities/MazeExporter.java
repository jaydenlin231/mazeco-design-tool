package com.mazeco.utilities;

import com.mazeco.exception.UnsolvableMazeException;
import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class allows any MazeModel to be converted to a BufferedImage then exported as a PNG image
 */
public final class MazeExporter {

    /**
     * This static method converts any MazeModel data into a BufferedImage equivalent.
     *
     * @param mazeModel Maze to be converted to BufferedImage.
     * @param cellSize  Size of each cell in the Maze in pixels.
     * @return A BufferedImage converted MazeModel.
     */
    public static BufferedImage paint(MazeModel mazeModel, int cellSize) {
        int width = mazeModel.getWidth() * cellSize;
        int height = mazeModel.getHeight() * cellSize;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        int x = 0;
        int y = 0;
        g.fillRect(0, 0, width - 1, height - 1);
        g.setColor(Color.BLACK);
        g.drawRect(1, 1, width - 2, height - 2);
        for (int i = 0; i < mazeModel.getWidth(); i++) {
            for (int j = 0; j < mazeModel.getHeight(); j++) {
                if (mazeModel.getBlock(i, j).equals(Block.WALL)) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, cellSize, cellSize);
                    y += cellSize;
                }
                if (mazeModel.getBlock(i, j).equals(Block.BLANK)) {
                    g.setColor(Color.WHITE);
                    g.fillRect(x, y, cellSize, cellSize);
                    y += cellSize;
                }
                if (mazeModel.getBlock(i, j).equals(Block.START)) {
                    if (mazeModel.getStartImage() != null) {
                        ImageIcon icon = new ImageIcon(mazeModel.getStartImage());
                        Image img = icon.getImage();
                        g.drawImage(img, x, y, cellSize, cellSize, null);
                    } else {
                        g.setColor(Color.RED);
                        g.fillRect(x, y, cellSize, cellSize);
                    }
                    y += cellSize;
                }
                if (mazeModel.getBlock(i, j).equals(Block.END)) {
                    if (mazeModel.getEndImage() != null) {
                        ImageIcon icon = new ImageIcon(mazeModel.getEndImage());
                        Image img = icon.getImage();
                        g.drawImage(img, x, y, cellSize, cellSize, null);
                    } else {
                        g.setColor(Color.GREEN);
                        g.fillRect(x, y, cellSize, cellSize);
                    }
                    y += cellSize;
                }
                if (mazeModel.getBlock(i, j).equals(Block.PATH)) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(x, y, cellSize, cellSize);
                    y += cellSize;
                }
                if (mazeModel.getBlock(i, j).equals(Block.LOGO)) {
                    y += cellSize;
                }

            }
            x += cellSize;
            y = 0;
        }
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
        if (mazeModel.getLogo() != null) {
            int logoX = mazeModel.getStartLogoPoint().x * cellSize;
            int logoY = mazeModel.getStartLogoPoint().y * cellSize;
            int LogoSize = (mazeModel.getEndLogoPoint().x - mazeModel.getStartLogoPoint().x + 1) * cellSize;

            ImageIcon icon = new ImageIcon(mazeModel.getLogo());
            Image img = icon.getImage();
            g.drawImage(img, logoX, logoY, LogoSize, LogoSize, null);
        }
        return image;
    }

    /**
     * This method converts any MazeModel into a shareable PNG format image with
     * either the solved solution or just plain maze to a path.
     *
     * @param path         The File path to the resulting PNG images.
     * @param name         The name of the maze.
     * @param firstName    The first name of the author.
     * @param lastName     The last name of the author.
     * @param date         The date of the maze.
     * @param mazeModel    The maze itself to be exported.
     * @param cellSize     Size of each cell in the Maze in pixels.
     * @param withSolution Boolean input to save with or without solution of the maze. True saves with solution.
     * @throws IOException             Incorrect file destination.
     * @throws UnsolvableMazeException If the maze is unsolvable.
     */
    public static void ExportPNG(File path, String name, String firstName, String lastName, String date, MazeModel mazeModel, int cellSize, boolean withSolution) throws IOException, UnsolvableMazeException {
        String solvedName = name + "Solved" + "_" + lastName + "_" + firstName + "_" + date + ".png";
        String fileNameClean = name + "_" + lastName + "_" + firstName + "_" + date + ".png";
        Path currentPath = Paths.get(String.valueOf(path));
        Path filePath = Paths.get(currentPath.toString(), fileNameClean);
        if (withSolution == true) {
            mazeModel.clearSolution();
            BufferedImage image = paint(mazeModel, cellSize);
            ImageIO.write(image, "png", new File(String.valueOf(filePath)));
            mazeModel.solve();
            BufferedImage imageSolved = paint(mazeModel, cellSize);
            filePath = Paths.get(currentPath.toString(), solvedName);
            ImageIO.write(imageSolved, "png", new File(String.valueOf(filePath)));
            mazeModel.clearSolution();
        } else {
            BufferedImage image = paint(mazeModel, cellSize);
            ImageIO.write(image, "png", new File(String.valueOf(filePath)));
        }
    }
}
