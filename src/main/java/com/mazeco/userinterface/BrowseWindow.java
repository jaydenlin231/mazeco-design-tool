package com.mazeco.userinterface;

import com.mazeco.database.MazeBrowserData;
import com.mazeco.models.MazeModel;
import com.mazeco.models.MazeRecord;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class BrowseWindow implements IUserInterface {
    private final String TITLE = "Browse";
    private final JFrame window = new JFrame(TITLE);
    private final JButton mazePreviewPlaceHold = new JButton("Maze Preview Place Holder");
    private final JButton sortMenuPlaceHold = new JButton("Sort Menu Place Holder");

    private final JButton editMazeButton = new JButton("Edit");
    private final JButton toggleSolutionButton = new JButton("Solution");
    private final JButton deleteMazeButton = new JButton("Delete");
    private final JButton exportMazeButton = new JButton("Export");
    private final JButton test = new JButton("test");

    private final JPanel leftPanel = new JPanel(new BorderLayout());
    private final JPanel rightPanel = new JPanel(new GridLayout(2, 1));
    private final JPanel mainPanel = new JPanel(new GridLayout(1, 2));
    
    private final JPanel mazePreviewPanel = new JPanel(new BorderLayout());

    private JLabel mazeIdDisplay;
    private JLabel mazeNameDisplay;
    private JLabel mazeAuthorDisplay;
    private JLabel mazeDateTimeCreatedDisplay;
    private JLabel mazeDateTimeModifiedDisplay;

    private final JPanel opButtonsPanel = new JPanel(new GridLayout(1, 3));

    private JList mazeList;

    MazeBrowserData data;

    public BrowseWindow(MazeBrowserData data) {
        this.data = data;

        initialisePanels();

        initialiseWindow();

        mazeList.setSelectedIndex(0);
    }

    private void initialiseWindow() {
        window.add(mainPanel);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setPreferredSize(new Dimension(1000, 800));
        window.setMinimumSize(new Dimension(500, 400));
        window.pack();
        // Centre the window
        window.setLocationRelativeTo(null);
    }

    private void initialiseMazeOpButtons(){
        editMazeButton.addActionListener(new MazeOpButtonActionListener());
        toggleSolutionButton.addActionListener(new MazeOpButtonActionListener());
        exportMazeButton.addActionListener(new MazeOpButtonActionListener());
        deleteMazeButton.addActionListener(new MazeOpButtonActionListener());
    }
    
    private void initialisePanels() {
        
        rightPanel.add(mazePreviewPlaceHold);
        rightPanel.add(initialiseMazeOpPanel());

        leftPanel.add(sortMenuPlaceHold, BorderLayout.NORTH);
        leftPanel.add(initialiseMazeListPanel(), BorderLayout.CENTER);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
    }

    private JPanel initialiseMazeOpPanel() {
        JPanel mazeOperationPanel = new JPanel(new BorderLayout());
        initialiseMazeOpButtons();
        opButtonsPanel.add(deleteMazeButton);
        opButtonsPanel.add(toggleSolutionButton);
        opButtonsPanel.add(exportMazeButton);
        
        mazeOperationPanel.add(initialiseMazeDetailPanel(), BorderLayout.CENTER);
        mazeOperationPanel.add(opButtonsPanel, BorderLayout.SOUTH);
        
        return mazeOperationPanel;
    }

    private JScrollPane initialiseMazeListPanel() {
        mazeList = new JList<MazeRecord>(data.getModel());
        mazeList.setFixedCellHeight(50);
        mazeList.addListSelectionListener(new MazeListListener());
  
        JScrollPane scroller = new JScrollPane(mazeList);
        scroller.setViewportView(mazeList);
        scroller
              .setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroller
              .setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // scroller.setMinimumSize(new Dimension(200, 150));
        // scroller.setPreferredSize(new Dimension(250, 150));
        // scroller.setMaximumSize(new Dimension(250, 200));

        return scroller;
    }

    private JPanel initialiseMazeDetailPanel() {
        JPanel mazeDetailPanel = new JPanel(new GridBagLayout());
        mazeDetailPanel.setBackground(Color.WHITE);
  
        JLabel mazeIdLabel = new JLabel("MazeCo");
        JLabel mazeNameLabel = new JLabel("Name");
        JLabel mazeAuthorLabel = new JLabel("Author");
        JLabel mazeDateTimeCreatedLabel = new JLabel("Created");
        JLabel mazeDateTimeModifiedLabel = new JLabel("Modified");
  
        mazeIdDisplay = new JLabel();
        mazeNameDisplay = new JLabel();
        mazeAuthorDisplay = new JLabel();
        mazeDateTimeCreatedDisplay = new JLabel();
        mazeDateTimeModifiedDisplay = new JLabel();

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1;
        constraints.weighty = 1;
        
        constraints.insets = new Insets(0, 10, 0, 10);

        // addToPanel(mazeDetailPanel, mazeIdLabel, constraints, 0, 0, 1, 1);
        addToPanel(mazeDetailPanel, mazeIdDisplay, constraints, 0, 0, 2, 1);
        addToPanel(mazeDetailPanel, mazeNameLabel, constraints, 0, 1, 1, 1);
        addToPanel(mazeDetailPanel, mazeAuthorLabel, constraints, 0, 2, 1, 1);
        addToPanel(mazeDetailPanel, mazeDateTimeCreatedLabel, constraints, 0, 3, 1, 1);
        addToPanel(mazeDetailPanel, mazeDateTimeModifiedLabel, constraints, 0, 4, 1, 1);
        
        constraints.anchor = GridBagConstraints.EAST;
        addToPanel(mazeDetailPanel, mazeNameDisplay, constraints, 1, 1, 1, 1);
        addToPanel(mazeDetailPanel, mazeAuthorDisplay, constraints, 1, 2, 1, 1);
        addToPanel(mazeDetailPanel, mazeDateTimeCreatedDisplay, constraints, 1, 3, 1, 1);
        addToPanel(mazeDetailPanel, mazeDateTimeModifiedDisplay, constraints, 1, 4, 1, 1);
  
        return mazeDetailPanel;
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

    private void showMazeRecordDetail(MazeRecord mazeRecord){
        if(mazeRecord == null)
            return;
        
        mazeIdDisplay.setText("<html><em><b>MazeCo#<b/><em/><html/>" + mazeRecord.getId().toString());
        mazeNameDisplay.setText(mazeRecord.getName());
        mazeAuthorDisplay.setText(mazeRecord.getAuthor().getFirstName() + " " + mazeRecord.getAuthor().getLastName());
        mazeDateTimeCreatedDisplay.setText(mazeRecord.getDateTimeCreated().toLocalDate().toString());
        mazeDateTimeModifiedDisplay.setText(mazeRecord.getDateTimeCreated().toLocalDate().toString());
    }

    @Override
    public void show() {
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    /**
    * Implements a ListSelectionListener for making the UI respond when a
    * different name is selected from the list.
    */
   private class MazeListListener implements ListSelectionListener {

    /**
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        // TODO Auto-generated method stub
            if (mazeList.getSelectedValue() != null) {
            //   display(data.get(mazeList.getSelectedValue()));
                MazeRecord selectedMazeRecord = (MazeRecord) mazeList.getSelectedValue();
                showMazeRecordDetail(selectedMazeRecord);
            }
        }
    }

    private class MazeOpButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Component source = (Component) e.getSource();
            if (source == deleteMazeButton) {
                handleDeleteMazeButton();
            } else if (source == exportMazeButton) {
                handleExportMazeButton();
            } else if (source == toggleSolutionButton) {
                handleToggleSolutionButton();
            } else if (source == editMazeButton) {
                handleEditMazeButton();
            }
        }

        private void handleDeleteMazeButton(){
            int index = mazeList.getSelectedIndex();
            data.remove((MazeRecord) mazeList.getSelectedValue());
            index--;
            if (index == -1) {
                if (data.getSize() != 0) {
                index = 0;
                }
            }
            mazeList.setSelectedIndex(index);
        }

        private void handleExportMazeButton(){

        }
        private void handleToggleSolutionButton(){

        }
        private void handleEditMazeButton(){

        }
    }
    
}
