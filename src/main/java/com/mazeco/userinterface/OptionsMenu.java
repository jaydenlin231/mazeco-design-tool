package com.mazeco.userinterface;

import com.mazeco.exception.InvalidMazeException;
import com.mazeco.models.MazeModel;
import com.mazeco.models.User;
import com.mazeco.utilities.CanvasMode;
import com.mazeco.utilities.MazeGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultFormatter;

public class OptionsMenu implements IUserInterface {
    private final static JFrame window = new JFrame();

    private final static JLabel mazeWidthLabel = new JLabel("Width", SwingConstants.CENTER);
    private final static JLabel mazeHeightLabel = new JLabel("Height", SwingConstants.CENTER);
    private final static JSpinner mazeWidthInput = new JSpinner(new SpinnerNumberModel(10, 10, 100, 1));
    private final static JSpinner mazeHeightInput = new JSpinner(new SpinnerNumberModel(10, 10, 100, 1));

    private final static JLabel mazeStartLabel = new JLabel("Start", SwingConstants.CENTER);
    private final static JLabel mazeEndLabel = new JLabel("End", SwingConstants.CENTER);
    private final static JSpinner mazeStartInput = new JSpinner(new SpinnerNumberModel(1, 1, 9, 2));
    private final static JSpinner mazeEndInput = new JSpinner(new SpinnerNumberModel(7, 1, 9, 2));

    private final static JLabel firstNameLabel = new JLabel("First Name", SwingConstants.CENTER);
    private final static JTextField firstNameField = new JTextField();
    private final static JLabel lastNameLabel = new JLabel("Last Name", SwingConstants.CENTER);
    private final static JTextField lastNameField = new JTextField();
    private final static JLabel mazeNameLabel = new JLabel("Maze Name", SwingConstants.CENTER);
    private final static JTextField mazeNameField = new JTextField();


    private final static JLabel startImageLabel = new JLabel("Start Image", SwingConstants.CENTER);
    private final static JLabel endImageLabel = new JLabel("End Image", SwingConstants.CENTER);
    private final static JLabel LogoImageLabel = new JLabel("Logo Image", SwingConstants.CENTER);
    private final static JButton startImageButton = new JButton("Select Image...");
    private final static JButton endImageButton = new JButton("Select Image...");
    private final static JButton logoImageButton = new JButton("Select Image...");

    private final static JButton generateButton = new JButton("Generate");

    private static MazeCanvas mazeCanvas;

    private static CanvasMode mode;

    private static String logoImage;
    private static String startImage;
    private static String endImage;

    private final static JPanel mainPanel = new JPanel(new GridBagLayout());

    private static OptionsMenu instance = null;

    private static GenerateButtonListener generateButtonListener;

    private OptionsMenu(CanvasMode mode) {
        OptionsMenu.mode = mode;
        generateButtonListener = new GenerateButtonListener(mode);
        generateButton.addActionListener(generateButtonListener);
        OptionsMenu.instance = this;
        initialiseButtonListeners();
        initialisePanel(mode);
        initialiseWindow(mode);
    }

    public static OptionsMenu getInstance(CanvasMode mode) {
        if (instance == null) {
            new OptionsMenu(mode);
        } else {
            OptionsMenu.mode = mode;
            reRenderPanel(mode);
            initialiseWindow(mode);
        }
        generateButton.removeActionListener(generateButtonListener);
        generateButtonListener = new GenerateButtonListener(mode);
        generateButton.addActionListener(generateButtonListener);

        return instance;
    }


    private static void initialiseWindow(CanvasMode mode) {
        window.setTitle(mode + " Configurator");
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.add(mainPanel);
        if (mode == CanvasMode.GENERATE) {
            window.setMinimumSize(new Dimension(400, 400));
        } else {
            window.setMinimumSize(new Dimension(380, 250));
        }
        window.repaint();
        window.setResizable(false);
        window.pack();
        // Centre the window
        window.setLocationRelativeTo(null);
    }

    private static void reRenderPanel(CanvasMode mode) {
        removeAllAndRepaintPanel();
        initialisePanel(mode);
        mainPanel.repaint();
        window.repaint();
        window.pack();
    }

    private static void removeAllAndRepaintPanel() {
        if (mainPanel != null) {
            mainPanel.removeAll();
            mainPanel.repaint();
        }
    }

    private static void initialisePanel(CanvasMode mode) {
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

    private void initialiseButtonListeners(){
        logoImageButton.addActionListener(new BrowseButtonListener());
        startImageButton.addActionListener(new BrowseButtonListener());
        endImageButton.addActionListener(new BrowseButtonListener());
    }

    public static void resetParameters() {
        mazeWidthInput.setValue(10);
        mazeHeightInput.setValue(10);
        mazeStartInput.setValue(1);
        mazeEndInput.setValue(7);
        mazeNameField.setText("");
        logoImage = null;
        startImage = null;
        endImage = null;
        startImageButton.setText("Select Image...");
        endImageButton.setText("Select Image...");
        logoImageButton.setText("Select Image...");
    }

    @Override
    public void show() {
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private static class BrowseButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton buttonSource = (JButton) e.getSource();
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Images", "jpg", "JPEG", "png");
            fileChooser.setFileFilter(filter);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (buttonSource == logoImageButton)
                    logoImage = file.getAbsolutePath();
                if (buttonSource == startImageButton)
                    startImage = file.getAbsolutePath();
                if (buttonSource == endImageButton)
                    endImage = file.getAbsolutePath();
                buttonSource.setText("Selected: " + file.getName().substring(0, Math.min(file.getName().length(), 15)));
            }
        }
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

    private static class GenerateButtonListener implements ActionListener {
        CanvasMode mode;
        public GenerateButtonListener(CanvasMode mode){
            this.mode = mode;
        }
        @Override
        public void actionPerformed(ActionEvent event) {
            MazeModel aModel = null;

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
            if ((Integer) mazeStartInput.getValue() % 2 == 0)
                mazeStartInput.setValue((Integer) mazeStartInput.getValue() - 1);
            try {
                Integer inputWidth = (Integer) mazeWidthInput.getValue();
                Integer inputHeight = (Integer) mazeHeightInput.getValue();
                Integer inputStartIndex = (Integer) mazeStartInput.getValue();
                Integer inputEndIndex = (Integer) mazeEndInput.getValue();
                if (!(inputStartIndex < inputWidth && inputEndIndex < inputWidth)) {

                }
                if (inputStartIndex < inputWidth && inputEndIndex < inputWidth && mode == CanvasMode.GENERATE) {
                    aModel = MazeGenerator.generateMaze(inputWidth, inputHeight, inputStartIndex, inputEndIndex, logoImage, startImage, endImage);
                } else if (inputStartIndex < inputWidth && inputEndIndex < inputWidth) {
                    aModel = new MazeModel(inputWidth, inputHeight, inputStartIndex, inputEndIndex, logoImage, startImage, endImage);
                }

            } catch (NumberFormatException exception) {
                exception.printStackTrace();
            } catch (InvalidMazeException exception){
                JOptionPane.showMessageDialog(window, "This particular maze configuration cannot be generated. Please select different parameters and try again.", "Error", JOptionPane.ERROR_MESSAGE);
                resetParameters();
            } catch (Exception exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(window, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
            mazeCanvas = MazeCanvas.getInstance(aModel, mode, mazeName, new User(firstName, lastName));
            mazeCanvas.show();

            resetParameters();
        }
    }

    private static void configSpinners(JSpinner spinner) {
        JComponent comp = spinner.getEditor();
        JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
        spinner.addChangeListener(new SpinnerChangeListener());
    }

    private static class SpinnerChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JSpinner source = (JSpinner) e.getSource();
            if (source == mazeWidthInput)
                handleWidthInput(source);
            else if (source == mazeHeightInput)
                handleHeightInput(source);
        }

        private static void handleWidthInput(JSpinner source) {
            mazeHeightInput.setValue(source.getValue());
            mazeStartInput.setModel(new SpinnerNumberModel(1, 1, (int) source.getValue() - 1, 2));
            mazeEndInput.setModel(new SpinnerNumberModel((int) source.getValue() - 3, 1, (int) source.getValue() - 1, 2));
        }
    
        private static void handleHeightInput(JSpinner source) {
            mazeWidthInput.setValue(source.getValue());
            mazeStartInput.setModel(new SpinnerNumberModel(1, 1, (int) source.getValue() - 1, 2));
            mazeEndInput.setModel(new SpinnerNumberModel((int) source.getValue() - 3, 1, (int) source.getValue() - 1, 2));
        }
    }

}
