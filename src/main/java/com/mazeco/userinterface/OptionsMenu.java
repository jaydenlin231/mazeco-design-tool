package com.mazeco.userinterface;

import com.mazeco.models.MazeModel;
import com.mazeco.models.User;
import com.mazeco.utilities.MazeGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Objects;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class OptionsMenu implements IUserInterface {
    private final JFrame window = new JFrame();

    private final JLabel mazeWidthLabel = new JLabel("Width", SwingConstants.CENTER);
    private final JLabel mazeHeightLabel = new JLabel("Height", SwingConstants.CENTER);
    private final JSpinner mazeWidthInput = new JSpinner(new SpinnerNumberModel(10, 10, 100, 1));
    private final JSpinner mazeHeightInput = new JSpinner(new SpinnerNumberModel(10, 10, 100, 1));

    private final JLabel mazeStartLabel = new JLabel("Start", SwingConstants.CENTER);
    private final JLabel mazeEndLabel = new JLabel("End", SwingConstants.CENTER);
    private final JSpinner mazeStartInput = new JSpinner(new SpinnerNumberModel(1, 1, 100, 2));
    private final JSpinner mazeEndInput = new JSpinner(new SpinnerNumberModel(7, 1, 100, 2));

    private final JLabel firstNameLabel = new JLabel("First Name", SwingConstants.CENTER);
    private final JTextField firstNameField = new JTextField();
    private final JLabel lastNameLabel = new JLabel("Last Name", SwingConstants.CENTER);
    private final JTextField lastNameField = new JTextField();
    private final JLabel mazeNameLabel = new JLabel("Maze Name", SwingConstants.CENTER);
    private final JTextField mazeNameField = new JTextField();


    private final JLabel startImageLabel = new JLabel("Start Image", SwingConstants.CENTER);
    private final JLabel endImageLabel = new JLabel("End Image", SwingConstants.CENTER);
    private final JLabel LogoImageLabel = new JLabel("Logo Image", SwingConstants.CENTER);
    private final JButton startImageButton = new JButton("Select Image...");
    private final JButton endImageButton = new JButton("Select Image...");
    private final JButton logoImageButton = new JButton("Select Image...");

    private final JButton generateButton = new JButton("Generate");

    private MazeCanvas mazeCanvas;

    private String options;

    private final JPanel mainPanel = new JPanel(new GridBagLayout());

    // Leave blank for Draw options or "Generate" for generate options
    public OptionsMenu(String options) {
        this.options = options;
        initialisePanel();
        initialiseWindow();
    }

    private void initialiseWindow() {
        window.setTitle(options + " Configurator");
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.add(mainPanel);
        window.pack();
        if (Objects.equals(options, "Generate")) {
            window.setMinimumSize(new Dimension(400, 370));
        } else {
            window.setMinimumSize(new Dimension(380, 250));
        }
        window.setResizable(false);
        // Centre the window
        window.setLocationRelativeTo(null);
    }

    private void initialisePanel() {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(1, 2, 1, 2);

        if (options.equals("Generate")) {
            addToPanel(mainPanel, mazeWidthLabel, constraints, 0, 0, 1, 1);
            addToPanel(mainPanel, mazeWidthInput, constraints, 1, 0, 1, 1);
            addToPanel(mainPanel, mazeHeightLabel, constraints, 2, 0, 1, 1);
            addToPanel(mainPanel, mazeHeightInput, constraints, 3, 0, 1, 1);
            addToPanel(mainPanel, mazeStartLabel, constraints, 0, 1, 1, 1);
            addToPanel(mainPanel, mazeStartInput, constraints, 1, 1, 1, 1);
            addToPanel(mainPanel, mazeEndLabel, constraints, 2, 1, 1, 1);
            addToPanel(mainPanel, mazeEndInput, constraints, 3, 1, 1, 1);
            addToPanel(mainPanel, startImageLabel, constraints, 0, 2, 1, 1);
            addToPanel(mainPanel, startImageButton, constraints, 1, 2, 3, 1);
            addToPanel(mainPanel, endImageLabel, constraints, 0, 3, 1, 1);
            addToPanel(mainPanel, endImageButton, constraints, 1, 3, 3, 1);
            addToPanel(mainPanel, LogoImageLabel, constraints, 0, 4, 1, 1);
            addToPanel(mainPanel, logoImageButton, constraints, 1, 4, 3, 1);
            addToPanel(mainPanel, firstNameLabel, constraints, 0, 5, 1, 1);
            addToPanel(mainPanel, firstNameField, constraints, 1, 5, 1, 1);
            addToPanel(mainPanel, lastNameLabel, constraints, 2, 5, 1, 1);
            addToPanel(mainPanel, lastNameField, constraints, 3, 5, 1, 1);
            addToPanel(mainPanel, mazeNameLabel, constraints, 0, 6, 1, 1);
            addToPanel(mainPanel, mazeNameField, constraints, 1, 6, 2, 1);
            addToPanel(mainPanel, generateButton, constraints, 1, 7, 2, 1);
        } else {
            addToPanel(mainPanel, mazeWidthLabel, constraints, 0, 0, 1, 1);
            addToPanel(mainPanel, mazeWidthInput, constraints, 1, 0, 1, 1);
            addToPanel(mainPanel, mazeHeightLabel, constraints, 2, 0, 1, 1);
            addToPanel(mainPanel, mazeHeightInput, constraints, 3, 0, 1, 1);
            addToPanel(mainPanel, mazeStartLabel, constraints, 0, 1, 1, 1);
            addToPanel(mainPanel, mazeStartInput, constraints, 1, 1, 1, 1);
            addToPanel(mainPanel, mazeEndLabel, constraints, 2, 1, 1, 1);
            addToPanel(mainPanel, mazeEndInput, constraints, 3, 1, 1, 1);
            addToPanel(mainPanel, firstNameLabel, constraints, 0, 2, 1, 1);
            addToPanel(mainPanel, firstNameField, constraints, 1, 2, 1, 1);
            addToPanel(mainPanel, lastNameLabel, constraints, 2, 2, 1, 1);
            addToPanel(mainPanel, lastNameField, constraints, 3, 2, 1, 1);
            addToPanel(mainPanel, mazeNameLabel, constraints, 0, 3, 1, 1);
            addToPanel(mainPanel, mazeNameField, constraints, 1, 3, 2, 1);
            addToPanel(mainPanel, generateButton, constraints, 1, 4, 2, 1);
        }
        generateButton.addActionListener(new GenerateButtonListener());
        mazeNameField.getDocument().addDocumentListener((TextFieldDocumentListener) e -> {
            mazeNameField.setBorder(UIManager.getBorder("TextField.border"));
        });
        firstNameField.getDocument().addDocumentListener((TextFieldDocumentListener) e -> {
            firstNameField.setBorder(UIManager.getBorder("TextField.border"));
        });
        lastNameField.getDocument().addDocumentListener((TextFieldDocumentListener) e -> {
            lastNameField.setBorder(UIManager.getBorder("TextField.border"));
        });

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

    private interface TextFieldDocumentListener extends DocumentListener {
        void update(DocumentEvent e);

        @Override
        default void insertUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        default void removeUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        default void changedUpdate(DocumentEvent e) {
            update(e);
        }
    }

    private class GenerateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            MazeModel aModel = null;
            MazeGenerator maze = null;

            String mazeName = mazeNameField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            if (mazeName.equals("") || firstName.equals("") || lastName.equals("")) {
                if (mazeName.equals(""))
                    mazeNameField.setBorder(new LineBorder(Color.RED, 1));
                if (firstName.equals(""))
                    firstNameField.setBorder(new LineBorder(Color.RED, 1));
                if (lastName.equals(""))
                    lastNameField.setBorder(new LineBorder(Color.RED, 1));
                return;
            }
            try {
                Integer w = (Integer) mazeWidthInput.getValue();
                Integer h = (Integer) mazeHeightInput.getValue();
                Integer s = (Integer) mazeStartInput.getValue();
                Integer e = (Integer) mazeEndInput.getValue();

                if (s < w && e < w && Objects.equals(options, "Generate")) {
                    maze = new MazeGenerator(w, h, s, e);
                } else if (s < w && e < w) {
                    aModel = new MazeModel(w, h, s, e);
                    System.out.println(aModel);
                }

            } catch (NumberFormatException e) {
                System.out.println("number error");
            }
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
            if (Objects.equals(options, "Generate")) {
                mazeCanvas = MazeCanvas.getInstance(maze.getMaze(), mazeName, new User(firstName, lastName, "tba", "tba"));
            } else {
                mazeCanvas = MazeCanvas.getInstance(aModel, mazeName, new User(firstName, lastName, "tba", "tba"));
            }
            mazeCanvas.show();
            resetParameters();
        }
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
