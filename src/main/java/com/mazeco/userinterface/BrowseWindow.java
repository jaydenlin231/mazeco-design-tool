package com.mazeco.userinterface;

import com.mazeco.models.MazeModel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BrowseWindow implements IUserInterface {
    private final String TITLE = "Browse";
    private final JFrame window = new JFrame(TITLE);
    private final JButton testButton = new JButton("Test button to edit");

    public BrowseWindow() {
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setMinimumSize(new Dimension(380, 200));
        window.add(testButton);
    }


    @Override
    public void show() {
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    
}
