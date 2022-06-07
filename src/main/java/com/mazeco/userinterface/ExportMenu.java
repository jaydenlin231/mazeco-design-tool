package com.mazeco.userinterface;

import com.mazeco.exception.UnsolvableMazeException;
import com.mazeco.models.MazeModel;
import com.mazeco.models.MazeRecord;
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
import java.util.ArrayList;

public class ExportMenu implements IUserInterface {
    private static final int DEFAULT_EXPORT_CELL_SIZE = 32;
    private final static JFrame window = new JFrame();
    private final static JPanel mainPanel = new JPanel(new GridBagLayout());

    private final static JLabel directoryLabel = new JLabel("Path", SwingConstants.CENTER);
    private final static JLabel cellSizeLabel = new JLabel("Cell Size", SwingConstants.CENTER);
    private final static JLabel solutionLabel = new JLabel("Solution", SwingConstants.CENTER);
    private final static JLabel cellSizeDescription = new JLabel("");

    private final static JSpinner cellSizeInput = new JSpinner(new SpinnerNumberModel(DEFAULT_EXPORT_CELL_SIZE, 1, 200, 1));
    private final static JButton exportButton = new JButton("Export");
    private final static JButton browseButton = new JButton("Choose folder...");
    private final static JToggleButton solutionButton = new JToggleButton("Saving with solution");

    private static File path;
    private static boolean saveWithSolution = true;
    private static ArrayList<MazeRecord> selectedMazeRecords;
    private static ExportMenu instance = null;

    private ExportMenu(ArrayList<MazeRecord> selectedMazeRecords) {
        ExportMenu.selectedMazeRecords = selectedMazeRecords;
        instance = this;
        initButtonListeners();
        initialisePanel(selectedMazeRecords);
        initialiseWindow(selectedMazeRecords);
    }

    public static ExportMenu getInstance(ArrayList<MazeRecord> selectedMazeRecords){
        if(instance == null){
            new ExportMenu(selectedMazeRecords);
        } else {
            ExportMenu.selectedMazeRecords = selectedMazeRecords;
            reRenderPanel(selectedMazeRecords);
            initialisePanel(selectedMazeRecords);
            initialiseWindow(selectedMazeRecords);
        }
        return instance;
    }

    private static void initialiseWindow(ArrayList<MazeRecord> selectedMazeRecords) {
        if(selectedMazeRecords.size() == 1)
            window.setTitle("Single Export");
        else
            window.setTitle("Multiple Export");

        window.addWindowListener(new ClosingListener());
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        window.add(mainPanel);
        window.pack();
        window.setMinimumSize(new Dimension(300, 170));
        window.setResizable(false);
        window.setLocationRelativeTo(null);
    }

    private static void initialisePanel(ArrayList<MazeRecord> selectedMazeRecords) {

        GridBagConstraints constraints = new GridBagConstraints();

        JComponent comp = cellSizeInput.getEditor();
        JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
        if(selectedMazeRecords.size() == 1)
            setCellSizeDescription((int) cellSizeInput.getValue());
        
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
        if(selectedMazeRecords.size() == 1)
            LayoutHelper.addToPanel(mainPanel, cellSizeDescription, constraints, 1, 2, 1, 1);
        LayoutHelper.addToPanel(mainPanel, solutionLabel, constraints, 0, 3, 1, 1);
        LayoutHelper.addToPanel(mainPanel, solutionButton, constraints, 1, 3, 1, 1);
        exportButton.setEnabled(false);
        LayoutHelper.addToPanel(mainPanel, exportButton, constraints, 1, 4, 1, 1);
    }

    private static void reRenderPanel(ArrayList<MazeRecord> selectedMazeRecords) {
        removeAllAndRepaintPanel();
        initialisePanel(selectedMazeRecords);
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

    private static void resetParameters() {
        path = null;
        browseButton.setText("Choose folder...");
        cellSizeInput.setValue(DEFAULT_EXPORT_CELL_SIZE);
        solutionButton.setText("Saving with solution");
        saveWithSolution = true;
        exportButton.setEnabled(false);
    }

    private static void setCellSizeDescription(int cellSize) {
        if(selectedMazeRecords.size() > 1)
            return;

        int width = selectedMazeRecords.get(0).getMazeModel().getWidth();
        int height = selectedMazeRecords.get(0).getMazeModel().getHeight();
        cellSizeDescription.setText("Your resulting image is: " + (cellSize * width) + " x " + (cellSize * height));
    }

    @Override
    public void show() {
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private void initButtonListeners(){
        solutionButton.addItemListener(new ToggleListener());
        cellSizeInput.addChangeListener(new SpinnerListener());
        exportButton.addActionListener(new ExportButtonListener());
        browseButton.addActionListener(new BrowseButtonListener());
    }

    private static class ExportButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            int cellSize = (int) cellSizeInput.getValue();
            try {
                for (MazeRecord mazeRecord : selectedMazeRecords) {
                    MazeModel maze = mazeRecord.getMazeModel();
                    String mazeName = mazeRecord.getName();
                    User user = mazeRecord.getAuthor();
                    String dateTime = mazeRecord.getDateTimeCreatedString("yyyy/MM/dd-HH-mm");
                    MazeExporter.ExportPNG(path, mazeName, user.getFirstName(), user.getLastName(), dateTime, maze, cellSize, saveWithSolution);
                    window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
                }
                resetParameters();
            } catch (IOException  e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                // e.printStackTrace();
            } catch (UnsolvableMazeException  e) {
                // Unsolvable mazes are never saved in DB
                throw new IllegalStateException();
            }
        }
    }

    private static class BrowseButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(window);
            if (option == JFileChooser.APPROVE_OPTION) {
                path = fileChooser.getSelectedFile();
                browseButton.setText("Selected: " + path.getName());
                exportButton.setEnabled(true);
            }
        }
    }

    private static class ToggleListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            int state = e.getStateChange();
            if (state == ItemEvent.SELECTED) {
                solutionButton.setText("Saving without solution");
                saveWithSolution = false;
            } else {
                solutionButton.setText("Saving with solution");
                saveWithSolution = true;
            }
        }
    }

    private static class SpinnerListener implements ChangeListener {
        public void stateChanged(ChangeEvent e) {
            JSpinner spinner = (JSpinner) e.getSource();
            Object value = spinner.getValue();
            if(selectedMazeRecords.size() == 1) {
                setCellSizeDescription((int) value);
            }
        }
    }

    private static class ClosingListener extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            resetParameters();
        }
    }
}
