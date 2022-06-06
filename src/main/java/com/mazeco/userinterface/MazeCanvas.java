package com.mazeco.userinterface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.sql.SQLException;
import java.util.UUID;

import com.mazeco.database.MazeBrowserData;
import com.mazeco.exception.UnsolvableMazeException;
import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;
import com.mazeco.models.MazeRecord;
import com.mazeco.models.User;
import com.mazeco.utilities.CanvasMode;
import com.mazeco.utilities.MazeExporter;
import com.mazeco.utilities.MazeGenerator;


public class MazeCanvas implements IUserInterface {
    private final static String TITLE = "Editor";
    private final static JFrame window = new JFrame(TITLE);

    private final static JButton saveBttn = new JButton("Save");
    private final static JButton logoBttn = new JButton("Place Logo");
    private final static JButton startImgBttn = new JButton("Place Start Image");
    private final static JButton endImgBttn = new JButton("Place End Image");
    private final static JButton regenBttn = new JButton("Regenerate Maze");
    private final static JButton checkBttn = new JButton("Check Maze");
    private final static JButton clearBttn = new JButton("Clear Maze");
    private final static JButton solutionBttn = new JButton("Toggle Solution");
    private final static JButton exportBttn = new JButton("Test Export");

    private static JPanel sidePanel = new JPanel(new GridLayout(14, 1, 1, 8));
    private static JPanel canvasPanel;
    private static MazeModel mazeModel;

    private static boolean isSolutionVisible = false;

    private static MazeCanvas instance = null;

    private static String mazeName = null;
    private static User user = null;

    private static CanvasMode mode;
    private static UUID currentMazeID;

    private MazeCanvas(MazeModel mazeModel, CanvasMode mode, String mazeName, User user) {
        if(instance != null)
            return;

        initSidePanelActionListeners();
        
        MazeCanvas.mazeModel = mazeModel;
        MazeCanvas.mazeName = mazeName;
        MazeCanvas.user = user;
        initialiseSidePanel(mode);
        MazeCanvas.canvasPanel = new JPanel(new GridLayout(mazeModel.getHeight(), mazeModel.getWidth()));
        renderCanvasPanelButtons();

        initialiseWindow();

        instance = this;
    }

    public static MazeCanvas getInstance(MazeModel mazeModel, CanvasMode mode, String mazeName, User user) {
        isSolutionVisible = false;
        MazeCanvas.mode = mode;
        MazeCanvas.mazeName = mazeName;
        MazeCanvas.user = user;
        
        if (instance == null) {
           new MazeCanvas(mazeModel, mode, mazeName, user);
        } else {
            MazeCanvas.mazeModel = mazeModel;
            MazeCanvas.canvasPanel.setLayout(new GridLayout(mazeModel.getHeight(), mazeModel.getWidth()));
            instance.reRenderSidePanel(mode);
            instance.reRenderCanvasPanel();
        }
        resetCheckSolSaveButtons();
        return instance;
     }

    public static MazeCanvas getInstance(MazeModel mazeModel, CanvasMode mode, UUID mazeID, String mazeName, User user) {
        instance = getInstance(mazeModel, mode, mazeName, user);
        MazeCanvas.currentMazeID = mazeID;
        return instance;
     }

    private void initSidePanelActionListeners() {
        clearBttn.addActionListener(new SideMenuActionListener());
        checkBttn.addActionListener(new SideMenuActionListener());
        saveBttn.addActionListener(new SideMenuActionListener());
        solutionBttn.addActionListener(new SideMenuActionListener());
        regenBttn.addActionListener(new SideMenuActionListener());
        exportBttn.addActionListener(new SideMenuActionListener());
    }

    private void initialiseSidePanel(CanvasMode mode) {
        sidePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        sidePanel.add(logoBttn);
        sidePanel.add(startImgBttn);
        sidePanel.add(endImgBttn);
        // Draw
        if (mode == CanvasMode.DRAW) {
            sidePanel.add(clearBttn);
            for (int i = 0; i < 7; i++) {
                JLabel blankPlaceHolder = new JLabel();
                sidePanel.add(blankPlaceHolder);
            }
        // Generate and Edit 
        } else {
            sidePanel.add(regenBttn);
            sidePanel.add(clearBttn);
            for (int i = 0; i < 6; i++) {
                JLabel blankPlaceHolder = new JLabel();
                sidePanel.add(blankPlaceHolder);
            }
        }
        solutionBttn.setEnabled(false);
        saveBttn.setEnabled(false);

        
        sidePanel.add(checkBttn);
        sidePanel.add(solutionBttn);
        sidePanel.add(saveBttn);
    }

    private void initialiseWindow() {
        window.addWindowListener(new ClosingListener());
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.setMinimumSize(new Dimension(900, 800));
        window.setLayout(new BorderLayout());
        window.setResizable(false);

        window.add(sidePanel, BorderLayout.WEST);
        window.add(canvasPanel, BorderLayout.CENTER);
        window.pack();
        
    }

    private void reRenderSidePanel(CanvasMode mode){
        removeAllAndRepaintSidePanel();
        initialiseSidePanel(mode);
        sidePanel.repaint();
        window.repaint();
        window.pack();
    }

    private void reRenderCanvasPanel(){
        removeAllAndRepaintCanvasPanel();
        renderCanvasPanelButtons();
        canvasPanel.repaint();
        window.repaint();
        window.pack();
    }

    private void renderCanvasPanelButtons() {
        for (int row = 0; row < mazeModel.getHeight(); row++) {
            for (int col = 0; col < mazeModel.getWidth(); col++) {
                JButton aBlockButton = new JButton();

                aBlockButton.addMouseListener(new MazeButtonActionListener());

                aBlockButton.setBorderPainted(true);
                aBlockButton.setOpaque(true);

                Block aBlockModel = mazeModel.getBlock(col, row);

                setBlockButtonStyle(aBlockButton, aBlockModel);

                aBlockButton.putClientProperty("position", new Point(col, row));
                aBlockButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                aBlockButton.setFocusPainted(false);

                canvasPanel.add(aBlockButton);
            }
        }
    }

    private void removeAllAndRepaintCanvasPanel() {
        if (canvasPanel != null) {
            canvasPanel.removeAll();
            canvasPanel.repaint();
        }
    }

    private void removeAllAndRepaintSidePanel() {
        if (sidePanel != null) {
            sidePanel.removeAll();
            sidePanel.repaint();
        }
    }

    private void resetCanvasMazeModel() {
        if (mazeModel != null) {
            mazeModel.resetData();
        }
    }

    private void setBlockButtonStyle(JButton aBlockButton, Block aBlock) {
        switch (aBlock) {
            case BLANK:
                aBlockButton.setBackground(Color.WHITE);
                break;

            case WALL:
                aBlockButton.setBackground(Color.BLACK);
                break;

            case START:
                aBlockButton.setBackground(Color.RED);
                break;
                
            case END:
                aBlockButton.setBackground(Color.GREEN);
                break;
                
            case PATH:
                if(isSolutionVisible)
                    aBlockButton.setBackground(Color.YELLOW);
                else
                    aBlockButton.setBackground(Color.WHITE);
                break;

            default:
                break;
        }

    }

    private static void resetCheckSolSaveButtons(){
        checkBttn.setEnabled(true);
        solutionBttn.setEnabled(false);
        saveBttn.setEnabled(false);
    }

    private void handleSanityCheckButton() throws UnsolvableMazeException {
        mazeModel.clearSolution();
        mazeModel.solve();

        isSolutionVisible = true;
        checkBttn.setEnabled(false);
        solutionBttn.setEnabled(true);
        saveBttn.setEnabled(true);

        reRenderCanvasPanel();
    }

    private void handleClearButton() {
        resetCanvasMazeModel();
        reRenderCanvasPanel();
        resetCheckSolSaveButtons();
    }

    private void handleRegenerateButton() {
        MazeCanvas.mazeModel = MazeGenerator.generateMaze(mazeModel.getWidth(), mazeModel.getHeight(), mazeModel.getStartX(), mazeModel.getEndX());
//        System.out.println(mazeModel);
        reRenderCanvasPanel();
        resetCheckSolSaveButtons();
    }

    private void handleSolutionButton() {
        isSolutionVisible = !isSolutionVisible;

        reRenderCanvasPanel();

    }

    private void handleSaveButton() throws SQLException, UnsolvableMazeException {
        mazeModel.clearSolution();
        ImageIcon cleanImage = new ImageIcon(MazeExporter.paint(mazeModel, 32));
        mazeModel.solve();
        ImageIcon solvedImage = new ImageIcon(MazeExporter.paint(mazeModel, 32));
        mazeModel.clearSolution();

        MazeBrowserData dbMazeBrowserData = MazeBrowserData.getInstance();
        if(mode == CanvasMode.EDIT && currentMazeID != null){
            System.out.println(currentMazeID);
            dbMazeBrowserData.update(currentMazeID, mazeModel, cleanImage, solvedImage);
        } else {
            dbMazeBrowserData.add(new MazeRecord(mazeName, user, mazeModel, cleanImage, solvedImage));
        }
        window.dispose();
        MazeBrowserData.sortMazeRecords(BrowseWindow.getSelectedMazeSortCriteria(), BrowseWindow.getSelectedMazeSortOrder());
        MazeBrowserData.reSyncMazeRecords();
        System.out.println("Saved");
    }

    @Override
    public void show() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                window.setLocationRelativeTo(null);
                window.setVisible(true);
            }
        });
    }

    private class SideMenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            Component source = (Component) e.getSource();
            try {
                if (source == saveBttn) {
                    handleSaveButton();
                } else if (source == clearBttn) {
                    handleClearButton();
                } else if (source == checkBttn) {
                    handleSanityCheckButton();
                } else if (source == solutionBttn) {
                    handleSolutionButton();
                } else if (source == regenBttn) {
                    handleRegenerateButton();
                }
            } catch (SQLException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                // JOptionPane.showMessageDialog(null, "Database file is corrupted. Please delete \"mazeco.db\" and restart the application.", "Error", JOptionPane.ERROR_MESSAGE);
                // System.exit(0);
            } catch (UnsolvableMazeException exception) {
                JOptionPane.showMessageDialog(window, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                saveBttn.setEnabled(false);
                solutionBttn.setEnabled(false);
            } catch (Exception exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(window, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private class MazeButtonActionListener implements MouseListener {
        @Override
        public void mouseEntered(MouseEvent e) {
            JButton aBlockButton = (JButton) e.getSource();
            Point clickedPosition = (Point) aBlockButton.getClientProperty("position");
            int pressedCol = (int) clickedPosition.getX();
            int pressedRow = (int) clickedPosition.getY();

            if (SwingUtilities.isLeftMouseButton(e)) {
                if (mazeModel.getBlock(pressedCol, pressedRow).equals(Block.BLANK)) {
                    setBlockButtonStyle(aBlockButton, Block.WALL);
                    mazeModel.setBlock(Block.WALL, pressedCol, pressedRow);
                    resetCheckSolSaveButtons();
                }
            } else if (SwingUtilities.isRightMouseButton(e)) {
                if (mazeModel.getBlock(pressedCol, pressedRow).equals(Block.WALL)) {
                    setBlockButtonStyle(aBlockButton, Block.BLANK);
                    mazeModel.setBlock(Block.BLANK, pressedCol, pressedRow);
                    resetCheckSolSaveButtons();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                JButton aBlockButton = (JButton) e.getSource();
                Point clickedPosition = (Point) aBlockButton.getClientProperty("position");
                int pressedCol = (int) clickedPosition.getX();
                int pressedRow = (int) clickedPosition.getY();

                resetCheckSolSaveButtons();


                switch (mazeModel.getBlock(pressedCol, pressedRow)) {
                    case BLANK:
                        setBlockButtonStyle(aBlockButton, Block.WALL);
                        mazeModel.setBlock(Block.WALL, pressedCol, pressedRow);
                        break;

                    case WALL:
                        setBlockButtonStyle(aBlockButton, Block.BLANK);
                        mazeModel.setBlock(Block.BLANK, pressedCol, pressedRow);
                        break;

                    case PATH:
                        setBlockButtonStyle(aBlockButton, Block.WALL);
                        mazeModel.setBlock(Block.WALL, pressedCol, pressedRow);
                        mazeModel.clearSolution();
                        
                        reRenderCanvasPanel();

                        break;

                    default:
                        break;
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

    }

    /**
    * Implements the windowClosing method from WindowAdapter/WindowListener to
    * persist the contents of the data/model.
    */
   private class ClosingListener extends WindowAdapter {
        /**
         * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
         */
        public void windowClosing(WindowEvent e) {
            try {
                if(mode == CanvasMode.EDIT){
                    MazeBrowserData.sortMazeRecords(BrowseWindow.getSelectedMazeSortCriteria(), BrowseWindow.getSelectedMazeSortOrder());
                    MazeBrowserData.reSyncMazeRecords();
                }
                int confirmation = JOptionPane.showConfirmDialog(window, 
                                                                 "Do you wish to exit? Your changes will be lost if you don't save them.", 
                                                                 "Warning", 
                                                                 JOptionPane.YES_NO_OPTION, 
                                                                 JOptionPane.WARNING_MESSAGE);
                
                if(confirmation == JOptionPane.YES_OPTION){
                    window.dispose();
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            } 
        }
    }

}