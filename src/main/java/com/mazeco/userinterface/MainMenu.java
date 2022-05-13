package com.mazeco.userinterface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.mazeco.models.MazeModel;

public class MainMenu implements IUserInterface {
    private static final String TITLE = "MazeCo Design Tool";
    private static final JFrame mainMenu = new JFrame(TITLE);

    private static final Icon generateIcon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/plus.png"));
    private static final JButton generateButton = new JButton("Generate", generateIcon);
    private static final Icon drawIcon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/pen.png"));
    private static final JButton drawButton = new JButton("Draw", drawIcon);
    private static final Icon browseIcon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/folder.jpeg"));
    private static final JButton browseButton = new JButton("Browse", browseIcon);


    JPanel leftPanel = new JPanel(new GridLayout(2, 1));
    JPanel rightPanel = new JPanel(new BorderLayout());
    JPanel mainPanel = new JPanel(new GridLayout(1, 2));


    private static BrowseWindow browseWindow = new BrowseWindow();
    private static OptionsMenu drawWindow = new OptionsMenu("Draw");
    private static OptionsMenu GenerateMenu = new OptionsMenu("Generate");

    public MainMenu() {
        initialiseButtons();

        generateButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        generateButton.setHorizontalTextPosition(SwingConstants.CENTER);
        drawButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        drawButton.setHorizontalTextPosition(SwingConstants.CENTER);
        browseButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        browseButton.setHorizontalTextPosition(SwingConstants.CENTER);


        leftPanel.add(generateButton);
        leftPanel.add(drawButton);

        rightPanel.add(browseButton, BorderLayout.CENTER);

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
                GenerateMenu.show();
                System.out.println("Generate");
            } else if (source == drawButton) {
                drawWindow.show();
                System.out.println("Draw");
            } else if (source == browseButton) {
                browseWindow.show();
                System.out.println("Browse");
            }
        }
    }
}
