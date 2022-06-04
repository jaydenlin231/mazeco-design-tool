package com.mazeco.userinterface;

import com.mazeco.models.MazeModel;
import com.mazeco.models.User;
import com.mazeco.utilities.MazeExporter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class ExportMenu implements IUserInterface {
    private final JFrame window = new JFrame();
    private final JPanel mainPanel = new JPanel(new GridBagLayout());

    private final JLabel directoryLabel = new JLabel("Path", SwingConstants.CENTER);
    private final JLabel cellSizeLabel = new JLabel("Cell Size", SwingConstants.CENTER);
    private final JLabel solutionLabel = new JLabel("Solution", SwingConstants.CENTER);
    private final JLabel cellSizeDescription = new JLabel("");

    private final JSpinner cellSizeInput = new JSpinner(new SpinnerNumberModel(32, 1, 200, 1));
    private final JButton exportButton = new JButton("Export");
    private final JButton browseButton = new JButton("Choose folder...");
    private final JToggleButton solutionButton = new JToggleButton("Saving with solution");


    private final MazeModel maze;
    private final String mazeName;
    private MazeExporter exporter;
    private final User user;
    private String dateTime;


    private File path;

    private boolean saveWithSolution = true;

    public ExportMenu(MazeModel mazeModel, String mazeName, User author, String dateTime) {
        this.maze = mazeModel;
        this.mazeName = mazeName;
        this.user = author;
        this.dateTime = dateTime;
        initialisePanel();
        initialiseWindow();
    }

    private void initialiseWindow() {
        window.setTitle("Export");
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.add(mainPanel);
        window.pack();
        window.setMinimumSize(new Dimension(300, 170));
        window.setResizable(false);
        window.setLocationRelativeTo(null);
    }

    private void initialisePanel() {

        GridBagConstraints constraints = new GridBagConstraints();

        JComponent comp = cellSizeInput.getEditor();
        JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);

        cellSizeDescription.setText("Your resulting image is: " + (int) cellSizeInput.getValue() * maze.getWidth() + " x " + (int) cellSizeInput.getValue() * maze.getHeight());
        solutionButton.setFocusPainted(false);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(1, 2, 1, 2);
        LayoutHelper.addToPanel(mainPanel, directoryLabel, constraints, 0, 0, 1, 1);
        LayoutHelper.addToPanel(mainPanel, browseButton, constraints, 1, 0, 1, 1);
        LayoutHelper.addToPanel(mainPanel, cellSizeLabel, constraints, 0, 1, 1, 1);
        LayoutHelper.addToPanel(mainPanel, cellSizeInput, constraints, 1, 1, 1, 1);
        LayoutHelper.addToPanel(mainPanel, cellSizeDescription, constraints, 1, 2, 1, 1);
        LayoutHelper.addToPanel(mainPanel, solutionLabel, constraints, 0, 3, 1, 1);
        LayoutHelper.addToPanel(mainPanel, solutionButton, constraints, 1, 3, 1, 1);
        LayoutHelper.addToPanel(mainPanel, exportButton, constraints, 1, 4, 1, 1);

        solutionButton.addItemListener(new ToggleListener());
        cellSizeInput.addChangeListener(new SpinnerListener());
        exportButton.addActionListener(new ExportButtonListener());
        browseButton.addActionListener(new BrowseButtonListener());

    }

    @Override
    public void show() {
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private class ExportButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                int cellSize = (int) cellSizeInput.getValue();
                exporter = new MazeExporter(maze, cellSize);
            } catch (NumberFormatException e) {
                System.out.println("Number error");
            }
            try {
                exporter.ExportPNG(path, mazeName, user.getFirstName(), user.getLastName(), dateTime, saveWithSolution);
                window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class BrowseButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(window);
            if (option == JFileChooser.APPROVE_OPTION) {
                path = fileChooser.getSelectedFile();
                browseButton.setText("Selected: " + path.getName());
            }
        }
    }

    private class ToggleListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            int state = e.getStateChange();
            if (state == e.SELECTED) {
                solutionButton.setText("Saving without solution");
                saveWithSolution = false;

            } else {
                solutionButton.setText("Saving with solution");
                saveWithSolution = true;
            }
        }
    }

    private class SpinnerListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            JSpinner spinner = (JSpinner) e.getSource();
            Object value = spinner.getValue();
            cellSizeDescription.setText("Your resulting image is: " + (int) value * maze.getWidth() + " x " + (int) value * maze.getHeight());
        }
    }
}
