package com.mazeco.userinterface;

import com.mazeco.models.MazeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Objects;
import javax.swing.JFrame;

public class OptionsMenu implements IUserInterface {
    private final JFrame window = new JFrame();

    private final JLabel mazeWidthLabel = new JLabel("Width", SwingConstants.CENTER);
    private final JLabel mazeHeightLabel = new JLabel("Height", SwingConstants.CENTER);
    private final JSpinner mazeWidthInput = new JSpinner(new SpinnerNumberModel(10, 10, 100, 1));
    private final JSpinner mazeHeightInput = new JSpinner(new SpinnerNumberModel(10, 10, 100, 1));

    private final JLabel startImageLabel = new JLabel("Start Image", SwingConstants.CENTER);
    private final JLabel endImageLabel = new JLabel("End Image", SwingConstants.CENTER);
    private final JLabel LogoImageLabel = new JLabel("Logo Image", SwingConstants.CENTER);
    private final JButton startImageButton = new JButton("Select Image...");
    private final JButton endImageButton = new JButton("Select Image...");
    private final JButton logoImageButton = new JButton("Select Image...");

    private final JButton generateButton = new JButton("Generate");

    private final JPanel mainPanel = new JPanel(new GridBagLayout());

    // Leave blank for Draw options or "Generate" for generate options
    public OptionsMenu(String options) {
        
        initialisePanel(options);

        initialiseWindow(options);

    }

    private void initialiseWindow(String options) {
        window.setTitle(options + " Configurator");
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.add(mainPanel);
        window.pack();
        if (Objects.equals(options, "Generate")) {
            window.setMinimumSize(new Dimension(400, 400));
        } else {
            window.setMinimumSize(new Dimension(380, 200));
        }
        window.setResizable(false);
        // Centre the window
        window.setLocationRelativeTo(null);
    }

    private void initialisePanel(String option) {
        generateButton.addActionListener(new ActionListener() {
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
                System.out.println(aModel);
                window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));

                MazeCanvas mazeCanvas = new MazeCanvas(aModel);
                mazeCanvas.show();

                resetParameters();
            }
        });

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(1, 2, 1, 2);

        if (option.equals("Generate")) {
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
            addToPanel(mainPanel, generateButton, constraints, 1, 4, 2, 1);
        } else {
            addToPanel(mainPanel, mazeWidthLabel, constraints, 0, 0, 1, 1);
            addToPanel(mainPanel, mazeWidthInput, constraints, 1, 0, 1, 1);
            addToPanel(mainPanel, mazeHeightLabel, constraints, 2, 0, 1, 1);
            addToPanel(mainPanel, mazeHeightInput, constraints, 3, 0, 1, 1);
            addToPanel(mainPanel, generateButton, constraints, 1, 1, 2, 1);
        }
    }

    public void resetParameters() {
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
