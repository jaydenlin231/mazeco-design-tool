package com.mazeco.userinterface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.io.FileWriter;
import java.io.IOException;

import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;

public class MazeCanvas implements IUserInterface, ActionListener {
    private static final String TITLE = "Editor";
    private static final JFrame window = new JFrame(TITLE);
    private static final JButton saveBttn = new JButton("Save");
    private static final JButton logoBttn = new JButton("Place Logo");
    private static final JButton startImgBttn = new JButton("Place Start Image");
    private static final JButton endImgBttn = new JButton("Place End Image");
    private static final JButton sizeBttn = new JButton("Change Maze Size");
    private static final JButton checkBttn = new JButton("Check Maze");
    private static final JButton clearBttn = new JButton("Clear Maze");
    private static JPanel sideMenu;
    private static MazeModel mazeModel;
    private static JPanel mazeCanvasPanel;


    public MazeCanvas() {
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.setMinimumSize(new Dimension(800, 800));
        window.setLayout(new BorderLayout());
        window.setResizable(false);
        // Centre the window
        window.setLocationRelativeTo(null);
        sideMenu = new JPanel();
        sideMenu.setBorder(new EmptyBorder(10, 10, 10, 10));
        sideMenu.setLayout(new GridLayout(14, 1, 1, 8));
        sideMenu.add(logoBttn);
        sideMenu.add(startImgBttn);
        sideMenu.add(endImgBttn);
        sideMenu.add(sizeBttn);
        sideMenu.add(clearBttn);

        clearBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                clearModel();
                show();
                System.out.println("Cleared");
            }
        });

        for (int i = 0; i < 7; i++) {
            JLabel placeHolder = new JLabel();
            sideMenu.add(placeHolder);
        }
        sideMenu.add(checkBttn);
        sideMenu.add(saveBttn);
        window.add(sideMenu, BorderLayout.WEST);

        saveBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    saveMaze(mazeModel);
                    window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
                    System.out.println("Saved");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MazeCanvas(MazeModel mazeModel){
        this();
        this.mazeModel = mazeModel;
    }

    public void render() {
        clearCanvas();

//        if (mazeModel == null) {
//            return;
//        }

        mazeCanvasPanel = new JPanel(new GridLayout(mazeModel.getHeight(), mazeModel.getWidth()));

        for (int i = 0; i < mazeModel.getHeight(); i++) {
            for (int j = 0; j < mazeModel.getWidth(); j++) {
                JButton aBlock = new JButton(j + ", " + i);
                aBlock.addActionListener(this);
                aBlock.setBorderPainted(false);
                aBlock.setOpaque(true);
                String block = mazeModel.getBlock(j, i).toString();
                if (block == "B") {
                    aBlock.setBackground(Color.WHITE);
                } else {
                    aBlock.setBackground(Color.BLACK);
                }
                mazeCanvasPanel.add(aBlock);
            }
        }
        window.add(mazeCanvasPanel, BorderLayout.CENTER);
        window.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        int pressedX = (int) (button.getBounds().x / button.getBounds().getWidth());
        int pressedY = (int) (button.getBounds().y / button.getBounds().getHeight());

        if (button.getBackground() == Color.WHITE) {
            button.setBackground(Color.BLACK);
            button.setForeground(Color.WHITE);
            mazeModel.setBlock(Block.WALL, pressedX, pressedY);
            System.out.println(mazeModel);

        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(Color.BLACK);
            mazeModel.setBlock(Block.BLANK, pressedX, pressedY);
            System.out.println(mazeModel);
        }
    }

    public void saveMaze(MazeModel maze) throws IOException {
        FileWriter writer = new FileWriter("./Mazes/saveTest.txt");

        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                Block aBlock = maze.getBlock(j, i);
                writer.write(aBlock.toString());
            }
            writer.write(System.getProperty("line.separator"));
        }
        writer.close();
    }

    public void clearCanvas() {
        if (mazeCanvasPanel != null) {
            window.remove(mazeCanvasPanel);
            window.repaint();

        }
    }

    public void clearModel() {
        if (mazeModel != null)
            mazeModel = new MazeModel(mazeModel.getWidth(), mazeModel.getHeight());
    }


    @Override
    public void show() {
        render();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}