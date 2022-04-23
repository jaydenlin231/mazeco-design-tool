package com.mazeco.userinterface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.mazeco.models.MazeModel;

public class MazeCanvas implements IUserInterface{
    private static final String TITLE = "Canvas";
    private static final JFrame window = new JFrame(TITLE);
    private static MazeModel mazeModel;
    private static JPanel mazeCanvasPanel;


    public MazeCanvas(){
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

    public void render(){
        clearCanvas();
        
        if(mazeModel== null){
            return;
        }

        mazeCanvasPanel = new JPanel(new GridLayout(mazeModel.getWidth(), mazeModel.getHeight()));
        // mazeCanvasPanel.removeAll();

        for (int i = 0; i < mazeModel.getWidth(); i++) {
            for (int j = 0; j < mazeModel.getHeight(); j++) {
                JButton aBlock = new JButton(i + ", " + j);
                mazeCanvasPanel.add(aBlock);
            }
        }
        mazeCanvasPanel.repaint();        
        mazeCanvasPanel.revalidate();        
        window.add(mazeCanvasPanel);
    }

    public void clearCanvas(){
        if(mazeCanvasPanel != null){
            window.remove(mazeCanvasPanel);
            window.repaint();
        }
    }

    public void clearModel(){
        if(mazeModel != null)
            mazeModel = null;
    }

    @Override
    public void show() {
        render();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

}