package com.mazeco.userinterface;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.*;

import com.mazeco.database.MazeBrowserData;
import com.mazeco.models.MazeModel;
import com.mazeco.utilities.CanvasMode;

public class MainMenu implements IUserInterface {
    private static final String TITLE = "MazeCo Design Tool";
    private static final JFrame window = new JFrame(TITLE);

    private static final Icon generateIcon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/laptop.png"));
    private static final Icon drawIcon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/pen.png"));
    private static final Icon browseIcon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/folder.jpeg"));
    
    private static final JButton generateButton = new JButton("Generate", generateIcon);
    private static final JButton drawButton = new JButton("Draw", drawIcon);
    private static final JButton browseButton = new JButton("Browse", browseIcon);

    JPanel leftPanel = new JPanel(new GridLayout(2, 1));
    JPanel rightPanel = new JPanel(new BorderLayout());
    JPanel mainPanel = new JPanel(new GridLayout(1, 2));

    private BrowseWindow browseWindow;
    private static OptionsMenu drawOptionsMenu = new OptionsMenu(CanvasMode.DRAW);
    private static OptionsMenu generateOptionsMenu = new OptionsMenu(CanvasMode.GENERATE);

    public MainMenu() {
        try {
            browseWindow = new BrowseWindow(MazeBrowserData.getInstance());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        

        initialiseButtons();

        initialisePanels();

        initialiseWindow();

    }

    private void initialiseWindow() {
        window.add(mainPanel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(750, 600));
        window.setMinimumSize(new Dimension(500, 400));
        window.pack();
        // Centre the window
        window.setLocationRelativeTo(null);
    }
    
    @Override
    public void show(){
        window.setVisible(true);
    }

    private void initialisePanels() {
        leftPanel.add(generateButton);
        leftPanel.add(drawButton);

        rightPanel.add(browseButton, BorderLayout.CENTER);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
    }

    private void initialiseButtons(){
        drawButton.setFocusPainted(false);
        generateButton.setFocusPainted(false);
        browseButton.setFocusPainted(false);
        drawButton.addActionListener(new MenuActionListener());
        generateButton.addActionListener(new MenuActionListener());
        browseButton.addActionListener(new MenuActionListener());
        generateButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        generateButton.setHorizontalTextPosition(SwingConstants.CENTER);
        drawButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        drawButton.setHorizontalTextPosition(SwingConstants.CENTER);
        browseButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        browseButton.setHorizontalTextPosition(SwingConstants.CENTER);
    }

    // Might need to create a separate class if we want to use these same actions at top menu bar.
    private class MenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
           Component source = (Component) e.getSource();
            if (source == generateButton) {
                generateOptionsMenu.show();
            } else if (source == drawButton) {
                drawOptionsMenu.show();
            } else if (source == browseButton) {
                browseWindow.show();
            }
        }
    }
}
