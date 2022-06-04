package com.mazeco.userinterface;

import com.mazeco.models.MazeModel;
import com.mazeco.models.User;
import com.mazeco.utilities.CanvasMode;
import com.mazeco.utilities.MazeGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultFormatter;

public class OptionsMenu implements IUserInterface {
    private final JFrame window = new JFrame();

    private final JLabel mazeWidthLabel = new JLabel("Width", SwingConstants.CENTER);
    private final JLabel mazeHeightLabel = new JLabel("Height", SwingConstants.CENTER);
    private final JSpinner mazeWidthInput = new JSpinner(new SpinnerNumberModel(10, 10, 100, 1));
    private final JSpinner mazeHeightInput = new JSpinner(new SpinnerNumberModel(10, 10, 100, 1));

    private final JLabel mazeStartLabel = new JLabel("Start", SwingConstants.CENTER);
    private final JLabel mazeEndLabel = new JLabel("End", SwingConstants.CENTER);
    private final JSpinner mazeStartInput = new JSpinner(new SpinnerNumberModel(1, 1, 9, 2));
    private final JSpinner mazeEndInput = new JSpinner(new SpinnerNumberModel(7, 1, 9, 2));

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

    private CanvasMode mode;

    private final JPanel mainPanel = new JPanel(new GridBagLayout());

    public OptionsMenu(CanvasMode mode) {
        this.mode = mode;
        initialisePanel(mode);

        initialiseWindow(mode);

    }

    private void initialiseWindow(CanvasMode mode) {
        window.setTitle(mode + " Configurator");
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.add(mainPanel);
        window.pack();
        if (mode == CanvasMode.GENERATE) {
            window.setMinimumSize(new Dimension(400, 400));
        } else {
            window.setMinimumSize(new Dimension(380, 250));
        }
        window.setResizable(false);
        // Centre the window
        window.setLocationRelativeTo(null);
    }

    private void initialisePanel(CanvasMode mode) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(1, 2, 1, 2);

        LayoutHelper.addToPanel(mainPanel, mazeWidthLabel, constraints, 0, 0, 1, 1);
        LayoutHelper.addToPanel(mainPanel, mazeWidthInput, constraints, 1, 0, 1, 1);
        LayoutHelper.addToPanel(mainPanel, mazeHeightLabel, constraints, 2, 0, 1, 1);
        LayoutHelper.addToPanel(mainPanel, mazeHeightInput, constraints, 3, 0, 1, 1);
        LayoutHelper.addToPanel(mainPanel, mazeStartLabel, constraints, 0, 1, 1, 1);
        LayoutHelper.addToPanel(mainPanel, mazeStartInput, constraints, 1, 1, 1, 1);
        LayoutHelper.addToPanel(mainPanel, mazeEndLabel, constraints, 2, 1, 1, 1);
        LayoutHelper.addToPanel(mainPanel, mazeEndInput, constraints, 3, 1, 1, 1);

        if (mode == CanvasMode.GENERATE) {
            LayoutHelper.addToPanel(mainPanel, startImageLabel, constraints, 0, 2, 1, 1);
            LayoutHelper.addToPanel(mainPanel, startImageButton, constraints, 1, 2, 3, 1);
            LayoutHelper.addToPanel(mainPanel, endImageLabel, constraints, 0, 3, 1, 1);
            LayoutHelper.addToPanel(mainPanel, endImageButton, constraints, 1, 3, 3, 1);
            LayoutHelper.addToPanel(mainPanel, LogoImageLabel, constraints, 0, 4, 1, 1);
            LayoutHelper.addToPanel(mainPanel, logoImageButton, constraints, 1, 4, 3, 1);
            LayoutHelper.addToPanel(mainPanel, firstNameLabel, constraints, 0, 5, 1, 1);
            LayoutHelper.addToPanel(mainPanel, firstNameField, constraints, 1, 5, 1, 1);
            LayoutHelper.addToPanel(mainPanel, lastNameLabel, constraints, 2, 5, 1, 1);
            LayoutHelper.addToPanel(mainPanel, lastNameField, constraints, 3, 5, 1, 1);
            LayoutHelper.addToPanel(mainPanel, mazeNameLabel, constraints, 0, 6, 1, 1);
            LayoutHelper.addToPanel(mainPanel, mazeNameField, constraints, 1, 6, 2, 1);
            LayoutHelper.addToPanel(mainPanel, generateButton, constraints, 1, 7, 2, 1);
        } else {
            LayoutHelper.addToPanel(mainPanel, firstNameLabel, constraints, 0, 2, 1, 1);
            LayoutHelper.addToPanel(mainPanel, firstNameField, constraints, 1, 2, 1, 1);
            LayoutHelper.addToPanel(mainPanel, lastNameLabel, constraints, 2, 2, 1, 1);
            LayoutHelper.addToPanel(mainPanel, lastNameField, constraints, 3, 2, 1, 1);
            LayoutHelper.addToPanel(mainPanel, mazeNameLabel, constraints, 0, 3, 1, 1);
            LayoutHelper.addToPanel(mainPanel, mazeNameField, constraints, 1, 3, 2, 1);
            LayoutHelper.addToPanel(mainPanel, generateButton, constraints, 1, 4, 2, 1);
        }
        generateButton.addActionListener(new GenerateButtonListener(mode));
        mazeNameField.getDocument().addDocumentListener((TextFieldDocumentListener) e -> {
            mazeNameField.setBorder(UIManager.getBorder("TextField.border"));
        });
        firstNameField.getDocument().addDocumentListener((TextFieldDocumentListener) e -> {
            firstNameField.setBorder(UIManager.getBorder("TextField.border"));
        });
        lastNameField.getDocument().addDocumentListener((TextFieldDocumentListener) e -> {
            lastNameField.setBorder(UIManager.getBorder("TextField.border"));
        });
        configSpinners(mazeWidthInput);
        configSpinners(mazeHeightInput);
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
        CanvasMode mode;
        public GenerateButtonListener(CanvasMode mode){
            this.mode = mode;
        }
        @Override
        public void actionPerformed(ActionEvent event) {
            MazeModel aModel = null;
            MazeGenerator maze = null;

            String mazeName = mazeNameField.getText();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            if (mazeName.trim().isEmpty() || firstName.trim().isEmpty() || lastName.trim().isEmpty()) {
                if (mazeName.trim().isEmpty())
                    mazeNameField.setBorder(new LineBorder(Color.RED, 1));
                if (firstName.trim().isEmpty())
                    firstNameField.setBorder(new LineBorder(Color.RED, 1));
                if (lastName.trim().isEmpty())
                    lastNameField.setBorder(new LineBorder(Color.RED, 1));
                return;
            }
            try {
                Integer w = (Integer) mazeWidthInput.getValue();
                Integer h = (Integer) mazeHeightInput.getValue();
                Integer s = (Integer) mazeStartInput.getValue();
                Integer e = (Integer) mazeEndInput.getValue();

                if (s < w && e < w && mode == CanvasMode.GENERATE) {
                    maze = new MazeGenerator(w, h, s, e);
                } else if (s < w && e < w) {
                    aModel = new MazeModel(w, h, s, e);
                    System.out.println(aModel);
                }

            } catch (NumberFormatException e) {
                System.out.println("number error");
            }
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
            if (mode == CanvasMode.GENERATE) {
                mazeCanvas = MazeCanvas.getInstance(maze.getMaze(), mode, mazeName, new User(firstName, lastName, "tba", "tba"));
            } else {
                mazeCanvas = MazeCanvas.getInstance(aModel, mode, mazeName, new User(firstName, lastName, "tba", "tba"));
            }
            mazeCanvas.show();
            resetParameters();
        }
    }

    private void configSpinners(JSpinner spinner) {
        JComponent comp = spinner.getEditor();
        JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
        spinner.addChangeListener(new SpinnerChangeListener());
    }

    private class SpinnerChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner source = (JSpinner) e.getSource();
            if (source == mazeWidthInput)
                handleWidthInput(source);
            else if (source == mazeHeightInput)
                handleHeightInput(source);
        }
    }

    private void handleWidthInput(JSpinner source) {
        mazeHeightInput.setValue(source.getValue());
        mazeStartInput.setModel(new SpinnerNumberModel(1, 1, (int) source.getValue() - 1, 2));
        mazeEndInput.setModel(new SpinnerNumberModel((int) source.getValue() - 3, 1, (int) source.getValue() - 1, 2));
    }

    private void handleHeightInput(JSpinner source) {
        mazeWidthInput.setValue(source.getValue());
        mazeStartInput.setModel(new SpinnerNumberModel(1, 1, (int) source.getValue() - 1, 2));
        mazeEndInput.setModel(new SpinnerNumberModel((int) source.getValue() - 3, 1, (int) source.getValue() - 1, 2));
    }

}
