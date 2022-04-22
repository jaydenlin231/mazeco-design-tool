package com.mazeco.userinterface;

import java.awt.*;
import javax.swing.*;

public class UIOrchestrator {
    private static final String TITLE = "MazeCo Design Tool";
    private JFrame mainMenu;

    public void run(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        
        initMainMenu();
    }

    private void initMainMenu(){
        mainMenu =  new JFrame(TITLE);
        
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        JButton generateButton = new JButton("Generate");
        JButton drawButton = new JButton("Draw");
        leftPanel.add(generateButton);
        leftPanel.add(drawButton);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        JButton browseButton = new JButton("Browse");
        // Fill available space with button
        rightPanel.add(browseButton, BorderLayout.CENTER);
        
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        mainMenu.add(mainPanel);
        mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu.setPreferredSize(new Dimension(1000, 800));
        mainMenu.setMinimumSize(new Dimension(500, 400));
        mainMenu.pack();
        // Centre the window
        mainMenu.setLocationRelativeTo(null);
        mainMenu.setVisible(true);
    }
}
