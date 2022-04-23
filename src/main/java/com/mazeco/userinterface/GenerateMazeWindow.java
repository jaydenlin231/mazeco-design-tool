package com.mazeco.userinterface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.mazeco.models.MazeModel;

public class GenerateMazeWindow implements IUserInterface{
    private static final String TITLE = "Generate Maze";
    private static final JFrame window = new JFrame(TITLE);
    
    private static final JLabel mazeWidthLabel = new JLabel("Width", SwingConstants.CENTER);
    private static final JLabel mazeHeightLabel = new JLabel("Height", SwingConstants.CENTER);
    private static final JSpinner mazeWidthInput = new JSpinner(new SpinnerNumberModel(10, 10, 100, 1));;
    private static final JSpinner mazeHeightInput = new JSpinner(new SpinnerNumberModel(10, 10, 100, 1));;
    private static final JButton generateButton = new JButton("Generate");;

    private static final JLabel startImageLabel = new JLabel("Start Image", SwingConstants.CENTER);
    private static final JLabel endImageLabel = new JLabel("End Image", SwingConstants.CENTER);
    private static final JLabel LogoImageLabel = new JLabel("Logo Image", SwingConstants.CENTER);

    private static final JButton startImageButton = new JButton("Select Image...");;
    private static final JButton endImageButton = new JButton("Select Image...");;
    private static final JButton logoImageButton = new JButton("Select Image...");;
    private static final JPanel mainPanel = new JPanel(new GridBagLayout());


    public GenerateMazeWindow(){
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // add components to grid
        GridBagConstraints constraints = new GridBagConstraints();
        // Defaults
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(5,10,5,10);
 
        addToPanel(mainPanel, mazeWidthLabel, constraints, 0, 0, 1, 1);
        addToPanel(mainPanel, mazeWidthInput, constraints, 1, 0, 1, 1);
        addToPanel(mainPanel, mazeHeightLabel, constraints, 2, 0, 1, 1);
        addToPanel(mainPanel, mazeHeightInput, constraints, 3, 0, 1, 1);

        addToPanel(mainPanel, startImageLabel, constraints, 0, 1, 1, 1);
        addToPanel(mainPanel, startImageButton, constraints, 1, 1, 3, 1);

        addToPanel(mainPanel, endImageLabel, constraints, 0, 2, 1, 1);
        addToPanel(mainPanel, endImageButton, constraints, 1, 2, 3, 1);

        addToPanel(mainPanel, LogoImageLabel, constraints, 0, 3, 1, 1);
        addToPanel(mainPanel, logoImageButton, constraints, 1, 3, 3, 1);
        
        generateButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event) {
                MazeModel aModel = null;
                try {
                    Integer w = (Integer) mazeWidthInput.getValue();
                    Integer h = (Integer) mazeHeightInput.getValue();
                    aModel = new MazeModel(w, h);
                } catch (NumberFormatException e) {
                    System.out.println("number error");
                }
                // BrowseWindow test = new BrowseWindow();
                // test.show();
                System.out.println(aModel);
                window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));

                MazeCanvas mazeCanvas = new MazeCanvas(aModel);
                mazeCanvas.show();

                resetParameters();
            }
        });

        addToPanel(mainPanel, generateButton, constraints, 0, 4, 4, 1);

        window.add(mainPanel);
        window.pack();

        // window.setPreferredSize(new Dimension(400, 600));
        window.setMinimumSize(new Dimension(400, 600));
        window.setResizable(false);
        // Centre the window
        window.setLocationRelativeTo(null);
    }

    public void resetParameters(){
        mazeWidthInput.setValue(10);
        mazeHeightInput.setValue(10);
    }

    @Override
    public void show() {
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        System.out.println(window);
    }

    /**
     *
     * A convenience method to add a component to given grid bag
     * layout locations. Code due to Cay Horstmann
     *
     * @param c           the component to add
     * @param constraints the grid bag constraints to use
     * @param x           the x grid position
     * @param y           the y grid position
     * @param w           the grid width of the component
     * @param h           the grid height of the component
     */
    private void addToPanel(JPanel jp, Component c, GridBagConstraints constraints, int x, int y, int w, int h) {
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = w;
        constraints.gridheight = h;
        jp.add(c, constraints);
    }

}
