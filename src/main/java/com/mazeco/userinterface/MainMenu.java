package com.mazeco.userinterface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenu implements IUserInterface{
    private static final String TITLE = "MazeCo Design Tool";
    private static final JFrame mainMenu = new JFrame(TITLE);

    private static final JButton generateButton = new JButton("Generate");
    private static final JButton drawButton = new JButton("Draw");
    private static final JButton browseButton = new JButton("Browse");

    private static GenerateMazeWindow generateMazeWindow = new GenerateMazeWindow();
    private static BrowseWindow browseWindow = new BrowseWindow();

    public MainMenu(){
        initialiseButtons();

        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.add(generateButton);
        leftPanel.add(drawButton);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
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

    }

    @Override
    public void show(){
        mainMenu.setVisible(true);
    }

    private void initialiseButtons(){
        drawButton.addActionListener(new MenuActionListener());
        generateButton.addActionListener(new MenuActionListener());
        browseButton.addActionListener(new MenuActionListener());
    }

    // Might need to create a separate class if we want to use these same actions at top menu bar.
    private class MenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
           Component source = (Component) e.getSource();
           if (source == generateButton) {
               generateMazeWindow.show();
            } else if (source == drawButton) {
               System.out.println("Draw");
            } else if (source == browseButton) {
                browseWindow.show();
               System.out.println("Browse");
           }
        }
    }
}