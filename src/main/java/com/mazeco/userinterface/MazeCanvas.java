package com.mazeco.userinterface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;

public class MazeCanvas implements IUserInterface, ActionListener {
    private static final String TITLE = "Canvas";
    private static final JFrame window = new JFrame(TITLE);
    private static MazeModel mazeModel;
    private static JPanel mazeCanvasPanel;


    public MazeCanvas() {
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.setMinimumSize(new Dimension(800, 800));
        window.setResizable(false);
        // Centre the window
        window.setLocationRelativeTo(null);
    }

    public MazeCanvas(MazeModel mazeModel){
        this();
        this.mazeModel = mazeModel;
    }

    public void render() {
        clearCanvas();

        if (mazeModel == null) {
            return;
        }

        mazeCanvasPanel = new JPanel(new GridLayout(mazeModel.getHeight(), mazeModel.getWidth()));

        for (int i = 0; i < mazeModel.getHeight(); i++) {
            for (int j = 0; j < mazeModel.getWidth(); j++) {
                JButton aBlock = new JButton(j + ", " + i);
                aBlock.addActionListener(this);
                aBlock.setBorderPainted(false);
                aBlock.setOpaque(true);
                aBlock.setBackground(Color.WHITE);

                mazeCanvasPanel.add(aBlock);
            }
        }
        window.add(mazeCanvasPanel);
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

    public void clearCanvas() {
        if (mazeCanvasPanel != null) {
            window.remove(mazeCanvasPanel);
            window.repaint();
        }
    }

    public void clearModel() {
        if (mazeModel != null)
            mazeModel = null;
    }


    @Override
    public void show() {
        render();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}