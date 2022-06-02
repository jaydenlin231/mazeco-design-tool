package com.mazeco.userinterface;

import com.mazeco.models.MazeModel;
import com.mazeco.utilities.MazeExporter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class ExportMenu implements IUserInterface {
    private final JFrame window = new JFrame();
    private final JPanel mainPanel = new JPanel(new GridBagLayout());

    private final JLabel directoryLabel = new JLabel("Path", SwingConstants.CENTER);
    private final JLabel cellSizeLabel = new JLabel("Cell Size", SwingConstants.CENTER);
    private final JLabel solutionLabel = new JLabel("Solution", SwingConstants.CENTER);
    private final JSpinner cellSizeInput = new JSpinner(new SpinnerNumberModel(32, 0, 200, 1));

    private final JButton exportButton = new JButton("Export");
    private JToggleButton solutionbutton = new JToggleButton("Saving with solution");

    private final MazeModel maze;
    private MazeExporter exporter;

    private boolean saveWithSolution = true;

    public ExportMenu(MazeModel mazeModel) {
        this.maze = mazeModel;
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
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    int cellSize = (int) cellSizeInput.getValue();
                    exporter = new MazeExporter(maze, cellSize);
                } catch (NumberFormatException e) {
                    System.out.println("Number error");
                }
                try {
                    exporter.ExportPNG("./Mazes/Clean.png", saveWithSolution);
                    window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(1, 2, 1, 2);
        addToPanel(mainPanel, directoryLabel, constraints, 0, 0, 1, 1);
        addToPanel(mainPanel, cellSizeLabel, constraints, 0, 1, 1, 1);
        addToPanel(mainPanel, cellSizeInput, constraints, 1, 1, 1, 1);
        addToPanel(mainPanel, solutionLabel, constraints, 0, 2, 1, 1);
        addToPanel(mainPanel, solutionbutton, constraints, 1, 2, 1, 1);
        solutionbutton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == e.SELECTED) {
                    solutionbutton.setText("Saving without solution");
                    saveWithSolution = false;

                } else {
                    solutionbutton.setText("Saving with solution");
                    saveWithSolution = true;
                }
            }
        });
        addToPanel(mainPanel, exportButton, constraints, 0, 3, 2, 1);
    }

    @Override
    public void show() {
        window.setLocationRelativeTo(null);
        window.setVisible(true);
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