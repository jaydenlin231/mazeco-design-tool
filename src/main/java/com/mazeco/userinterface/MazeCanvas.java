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
    private final String TITLE = "Editor";
    private final JFrame window = new JFrame(TITLE);
    private final JButton saveBttn = new JButton("Save");
    private final JButton logoBttn = new JButton("Place Logo");
    private final JButton startImgBttn = new JButton("Place Start Image");
    private final JButton endImgBttn = new JButton("Place End Image");
    private final JButton sizeBttn = new JButton("Change Maze Size");
    private final JButton checkBttn = new JButton("Check Maze");
    private final JButton clearBttn = new JButton("Clear Maze");
    private JPanel sideMenu;
    private MazeModel mazeModel;
    private JPanel mazeCanvasPanel;

    public MazeCanvas(MazeModel mazeModel) {
        this.mazeModel = mazeModel;

        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setMinimumSize(new Dimension(900, 800));
        window.setLayout(new BorderLayout());
        window.setResizable(true);

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
                render();
                System.out.println("Cleared");
            }
        });
        for (int i = 0; i < 7; i++) {
            JLabel placeHolder = new JLabel();
            sideMenu.add(placeHolder);
        }
        sideMenu.add(checkBttn);
        sideMenu.add(saveBttn);
        saveBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    saveMaze(mazeModel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                window.dispose();
                System.out.println("Saved");
            }
        });
        window.add(sideMenu, BorderLayout.WEST);

        render();
    }

    public void render() {
        clearCanvas();
        mazeCanvasPanel = new JPanel(new GridLayout(mazeModel.getHeight(), mazeModel.getWidth()));

        for (int i = 0; i < mazeModel.getHeight(); i++) {
            for (int j = 0; j < mazeModel.getWidth(); j++) {
                JButton aBlock = new JButton();
                aBlock.addActionListener(this);
                aBlock.setBorderPainted(true);
                aBlock.setOpaque(true);
                String block = mazeModel.getBlock(j, i).toString();
                if (block.equals("B")) {
                    aBlock.setBackground(Color.WHITE);
                } else {
                    aBlock.setBackground(Color.BLACK);
                }
                aBlock.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                aBlock.setFocusPainted(false);

                if (mazeModel.getHeight() <= 11) {
                    aBlock.setPreferredSize(new Dimension(80, 80));
                } else if (mazeModel.getHeight() <= 15) {
                    aBlock.setPreferredSize(new Dimension(55, 55));
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
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}