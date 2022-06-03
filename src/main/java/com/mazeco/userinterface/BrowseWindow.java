package com.mazeco.userinterface;

import com.mazeco.database.MazeBrowserData;
import com.mazeco.models.MazeModel;
import com.mazeco.models.MazeRecord;
import com.mazeco.utilities.CanvasMode;
import com.mazeco.utilities.SortCriteria;
import com.mazeco.utilities.SortOrder;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class BrowseWindow implements IUserInterface {
    private final String TITLE = "Browse";
    private final JFrame window = new JFrame(TITLE);
    private final JPanel mazePreviewWindow = new JPanel();
    private final SortCriteria[] mazeSortOptions = SortCriteria.class.getEnumConstants();
    private final JComboBox<SortCriteria> mazeSortDropdown = new JComboBox<SortCriteria>(mazeSortOptions);
    private SortCriteria selectedMazeSortCriteria;
    private SortOrder selectedMazeSortOrder = SortOrder.ASC;
    private final JButton mazeSortOrderButton = new JButton(selectedMazeSortOrder.toString());

    private final Icon editIcon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/pen2.png"));
    private final Icon deleteIcon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/trash.png"));
    private Icon toggleSolutionIcon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/toggleOff.png"));
    private final Icon exportMazeIcon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/export.png"));


    private final JButton editMazeButton = new JButton("Edit", editIcon);
    private JButton toggleSolutionButton = new JButton("Solution", toggleSolutionIcon);
    private final JButton deleteMazeButton = new JButton("Delete", deleteIcon);
    private final JButton exportMazeButton = new JButton("Export", exportMazeIcon);
//    private final JButton test = new JButton("test");

    private final JPanel leftPanel = new JPanel(new BorderLayout());
    private final JPanel rightPanel = new JPanel(new GridLayout(2, 1));
    private final JPanel mainPanel = new JPanel(new GridLayout(1, 2));

    private JButton mazePreviewButton = new JButton();
    private boolean isSolutionVisible = false;

    private JLabel mazeNameDisplay;
    private JLabel mazeSizeDisplay;
    private JLabel mazeAuthorDisplay;
    private JLabel mazeDateTimeCreatedDisplay;
    private JLabel mazeDateTimeModifiedDisplay;

    private final JPanel opButtonsPanel = new JPanel(new GridLayout(1, 4));

    private JList<MazeRecord> mazeList;

    private MazeBrowserData data;

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
        window.setResizable(false);
        window.setLocationRelativeTo(null);
    }

    private void initialiseMazeOpButtons() {
        opButtonsPanel.setPreferredSize(new Dimension(400, 80));
        opButtonsPanel.setBackground(Color.white);
        opButtonsPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.lightGray));
        editMazeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        editMazeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        deleteMazeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        deleteMazeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        toggleSolutionButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        toggleSolutionButton.setHorizontalTextPosition(SwingConstants.CENTER);
        exportMazeButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        exportMazeButton.setHorizontalTextPosition(SwingConstants.CENTER);
        editMazeButton.setBorderPainted(false);
        editMazeButton.setFocusPainted(false);
        deleteMazeButton.setBorderPainted(false);
        deleteMazeButton.setFocusPainted(false);
        toggleSolutionButton.setBorderPainted(false);
        toggleSolutionButton.setFocusPainted(false);
        exportMazeButton.setBorderPainted(false);
        exportMazeButton.setFocusPainted(false);
        deleteMazeButton.setMargin(new Insets(10, 1, 10, 1));
        editMazeButton.addActionListener(new MazeOpButtonActionListener());
        mazePreviewButton.addActionListener(new MazeOpButtonActionListener());
        mazePreviewButton.setBorderPainted(false);
        mazePreviewButton.setFocusPainted(false);
        toggleSolutionButton.addActionListener(new MazeOpButtonActionListener());
        exportMazeButton.addActionListener(new MazeOpButtonActionListener());
        deleteMazeButton.addActionListener(new MazeOpButtonActionListener());
    }
    
    private void initialisePanels() {
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(mazePreviewButton);
        rightPanel.add(initialiseMazeOpPanel());
        leftPanel.add(initialiseMazeSortPanel(), BorderLayout.NORTH);
        leftPanel.add(initialiseMazeListPanel(), BorderLayout.CENTER);
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
    }

    private JPanel initialiseMazeOpPanel() {
        JPanel mazeOperationPanel = new JPanel(new BorderLayout());
        initialiseMazeOpButtons();

        opButtonsPanel.add(editMazeButton);
        opButtonsPanel.add(deleteMazeButton);
        opButtonsPanel.add(toggleSolutionButton);
        opButtonsPanel.add(exportMazeButton);
        
        mazeOperationPanel.add(initialiseMazeDetailPanel(), BorderLayout.CENTER);
        mazeOperationPanel.add(opButtonsPanel, BorderLayout.SOUTH);
        
        return mazeOperationPanel;
    }

    private JPanel initialiseMazeSortPanel(){
        JPanel mazeSortPanel = new JPanel(new GridLayout(1, 2));
        mazeSortDropdown.addItemListener(new MazeSortListener());
        mazeSortOrderButton.addActionListener(new MazeSortListener());
        mazeSortPanel.add(mazeSortDropdown);
        mazeSortPanel.add(mazeSortOrderButton);
        
        return mazeSortPanel;
    }

    private JScrollPane initialiseMazeListPanel() {
        mazeList = new JList<MazeRecord>(data.getModel());
        mazeList.setFixedCellHeight(30);
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
  
        JLabel mazeDetailsLabel = new JLabel("<html><b> Maze Details </b></html>");
        JLabel mazeNameLabel = new JLabel("Name");
        JLabel mazeSizeLabel = new JLabel("Maze Size");
        JLabel mazeAuthorLabel = new JLabel("Author");
        JLabel mazeDateTimeCreatedLabel = new JLabel("Created");
        JLabel mazeDateTimeModifiedLabel = new JLabel("Modified");

        mazeNameDisplay = new JLabel("...");
        mazeSizeDisplay = new JLabel("...");
        mazeAuthorDisplay = new JLabel("...");
        mazeDateTimeCreatedDisplay = new JLabel("...");
        mazeDateTimeModifiedDisplay = new JLabel("...");

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1;
        constraints.weighty = 1;

        constraints.insets = new Insets(0, 10, 0, 10);

        // addToPanel(mazeDetailPanel, mazeIdLabel, constraints, 0, 0, 1, 1);
        addToPanel(mazeDetailPanel, mazeDetailsLabel, constraints, 0, 0, 2, 1);
        addToPanel(mazeDetailPanel, mazeNameLabel, constraints, 0, 1, 1, 1);
        addToPanel(mazeDetailPanel, mazeSizeLabel, constraints, 0, 2, 1, 1);
        addToPanel(mazeDetailPanel, mazeAuthorLabel, constraints, 0, 3, 1, 1);
        addToPanel(mazeDetailPanel, mazeDateTimeCreatedLabel, constraints, 0, 4, 1, 1);
        addToPanel(mazeDetailPanel, mazeDateTimeModifiedLabel, constraints, 0, 5, 1, 1);
        
        constraints.anchor = GridBagConstraints.EAST;
        addToPanel(mazeDetailPanel, mazeNameDisplay, constraints, 1, 1, 1, 1);
        addToPanel(mazeDetailPanel, mazeSizeDisplay, constraints, 1, 2, 1, 1);
        addToPanel(mazeDetailPanel, mazeAuthorDisplay, constraints, 1, 3, 1, 1);
        addToPanel(mazeDetailPanel, mazeDateTimeCreatedDisplay, constraints, 1, 4, 1, 1);
        addToPanel(mazeDetailPanel, mazeDateTimeModifiedDisplay, constraints, 1, 5, 1, 1);

        return mazeDetailPanel;
    }


    private void displayMazeRecordDetail(MazeRecord mazeRecord){
        if(mazeRecord == null)
            return;

        mazePreviewButton.setText("<html><em><b>MazeCo#<b/><em/><html/>" + mazeRecord.getId().toString());
        mazePreviewButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        mazePreviewButton.setHorizontalTextPosition(SwingConstants.CENTER);
        mazeNameDisplay.setText(mazeRecord.getName());
        mazeSizeDisplay.setText(mazeRecord.getMazeModel().getWidth() + " x " + mazeRecord.getMazeModel().getHeight());
        mazeAuthorDisplay.setText(mazeRecord.getAuthor().getFirstName() + " " + mazeRecord.getAuthor().getLastName());
        mazeDateTimeCreatedDisplay.setText(mazeRecord.getDateTimeCreatedString("yyyy-MM-dd HH:mm:ss"));
        mazeDateTimeModifiedDisplay.setText(mazeRecord.getDateTimeModifiedString("yyyy-MM-dd HH:mm:ss"));
        Image scaledCleanImage = mazeRecord.getCleanImage().getImage().getScaledInstance(320, 320, java.awt.Image.SCALE_SMOOTH);
        Image scaledSolvedImage = mazeRecord.getSolvedImage().getImage().getScaledInstance(320, 320, java.awt.Image.SCALE_SMOOTH);
        if (isSolutionVisible == false)
            mazePreviewButton.setIcon(new ImageIcon(scaledCleanImage));
        else
            mazePreviewButton.setIcon(new ImageIcon(scaledSolvedImage));
    }

    private void resetMazeRecordDetail(){
        mazePreviewButton.setText("");
        mazeNameDisplay.setText("...");
        mazeSizeDisplay.setText("...");
        mazeAuthorDisplay.setText("...");
        mazeDateTimeCreatedDisplay.setText("...");
        mazeDateTimeModifiedDisplay.setText("...");
        mazePreviewButton.setIcon(new ImageIcon());
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
            if (mazeList.getSelectedValue() != null) {
                MazeRecord selectedMazeRecord = (MazeRecord) mazeList.getSelectedValue();
                displayMazeRecordDetail(selectedMazeRecord);
            }
        }
    }

    private class MazeSortListener implements ItemListener, ActionListener{
        @Override
        public void itemStateChanged(ItemEvent event) {
           if (event.getStateChange() == ItemEvent.SELECTED) {
            selectedMazeSortCriteria = (SortCriteria)event.getItem();
              
            try {
                if(selectedMazeSortCriteria == null || selectedMazeSortOrder == null)
                    return;
                
                    MazeBrowserData.sortMazeRecords(selectedMazeSortCriteria, selectedMazeSortOrder);
            } catch (SQLException e) {
                e.printStackTrace();
            }
           }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if(selectedMazeSortCriteria == null || selectedMazeSortOrder == null)
                    return;

                if(selectedMazeSortOrder == SortOrder.ASC){
                    MazeBrowserData.sortMazeRecords(selectedMazeSortCriteria, SortOrder.DSC);
                    selectedMazeSortOrder = SortOrder.DSC;
                    mazeSortOrderButton.setText(selectedMazeSortOrder.toString());
                    return;
                }
                
                MazeBrowserData.sortMazeRecords(selectedMazeSortCriteria, SortOrder.ASC);
                selectedMazeSortOrder = SortOrder.ASC;
                mazeSortOrderButton.setText(selectedMazeSortOrder.toString());
            } catch (SQLException e1) {
                e1.printStackTrace();
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
            } else if (source == editMazeButton || source == mazePreviewButton) {
                handleEditMazeButton();
            } else if (source == exportMazeButton) {
                handleExportMazeButton();
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
                resetMazeRecordDetail();
            }
            mazeList.setSelectedIndex(index);
        }

        private void handleExportMazeButton() {
            if (mazeList.getSelectedValue() == null)
                return;

            MazeRecord selectedMazeRecord = (MazeRecord) mazeList.getSelectedValue();

            ExportMenu exportMenu = new ExportMenu(selectedMazeRecord.getMazeModel(), selectedMazeRecord.getName());
            exportMenu.show();
        }

        private void handleToggleSolutionButton() {
            if (mazeList.getSelectedValue() == null)
                return;

            MazeRecord selectedMazeRecord = (MazeRecord) mazeList.getSelectedValue();
            Image scaledSolvedImage = selectedMazeRecord.getSolvedImage().getImage().getScaledInstance(320, 320, java.awt.Image.SCALE_SMOOTH);
            Image scaledCleanImage = selectedMazeRecord.getCleanImage().getImage().getScaledInstance(320, 320, java.awt.Image.SCALE_SMOOTH);

            mazePreviewButton.setIcon(new ImageIcon(scaledSolvedImage));

            isSolutionVisible = !isSolutionVisible;

            if (isSolutionVisible) {
                toggleSolutionButton.setIcon(new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/toggleOn.png")));
                mazePreviewButton.setIcon(new ImageIcon(scaledSolvedImage));

            } else {
                toggleSolutionButton.setIcon(new ImageIcon(Thread.currentThread().getContextClassLoader().getResource("images/toggleOff.png")));
                mazePreviewButton.setIcon(new ImageIcon(scaledCleanImage));
            }
        }
        private void handleEditMazeButton(){
            if (mazeList.getSelectedValue() == null) 
                return;

            MazeRecord selectedMazeRecord = (MazeRecord) mazeList.getSelectedValue();

            MazeCanvas mazeCanvas = MazeCanvas.getInstance(selectedMazeRecord.getMazeModel(), CanvasMode.EDIT, selectedMazeRecord.getId(),selectedMazeRecord.getName(), selectedMazeRecord.getAuthor());
            mazeCanvas.show();
        }
    }
    
}
