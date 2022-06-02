package com.mazeco.userinterface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import com.mazeco.database.MazeBrowserData;
import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;
import com.mazeco.models.MazeRecord;
import com.mazeco.models.User;
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

    private MazeCanvas(MazeModel mazeModel) {
        if(instance != null)
            return;

        MazeCanvas.mazeModel = mazeModel;
        if (mazeModel.getBlock(0, 0).equals(Block.WALL)) {
            initialiseSidePanel(true);
        } else {
            initialiseSidePanel(false);

        }
        MazeCanvas.canvasPanel = new JPanel(new GridLayout(mazeModel.getHeight(), mazeModel.getWidth()));
        renderCanvasPanelButtons();

        initialiseWindow();

        instance = this;
    }

    private void initialiseSidePanel(boolean regenerateButton) {
        sidePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        sidePanel.add(logoBttn);
        sidePanel.add(startImgBttn);
        sidePanel.add(endImgBttn);
        if (regenerateButton) {
            sidePanel.add(regenBttn);
            sidePanel.add(clearBttn);
            for (int i = 0; i < 6; i++) {
                JLabel blankPlaceHolder = new JLabel();
                sidePanel.add(blankPlaceHolder);
            }
        } else {
            sidePanel.add(clearBttn);
            for (int i = 0; i < 7; i++) {
                JLabel blankPlaceHolder = new JLabel();
                sidePanel.add(blankPlaceHolder);
            }
        }
        sidePanel.add(checkBttn);
        sidePanel.add(solutionBttn);
        sidePanel.add(saveBttn);

        clearBttn.addActionListener(new SideMenuActionListener());
        checkBttn.addActionListener(new SideMenuActionListener());
        saveBttn.addActionListener(new SideMenuActionListener());
        solutionBttn.addActionListener(new SideMenuActionListener());
        regenBttn.addActionListener(new SideMenuActionListener());
        exportBttn.addActionListener(new SideMenuActionListener());
        
    }

    private void initialiseWindow() {
        window.addWindowListener(new ClosingListener());
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setMinimumSize(new Dimension(900, 800));
        window.setLayout(new BorderLayout());
        window.setResizable(false);

        window.add(sidePanel, BorderLayout.WEST);
        window.add(canvasPanel, BorderLayout.CENTER);
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

                aBlockButton.addActionListener(new MazeButtonActionListener());
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

    private void saveMaze(MazeModel maze) throws IOException {
        FileWriter writer = new FileWriter("./Mazes/saveTest.txt");

        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                Block aBlock = maze.getBlock(j, i);
                writer.write(aBlock.toString());
            }
            writer.write(System.getProperty("line.separator"));
        }
        writer.close();
    }

    private void removeAllAndRepaintCanvasPanel() {
        if (canvasPanel != null) {
            canvasPanel.removeAll();
            canvasPanel.repaint();
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

    private void handleSanityCheckButton() {
        mazeModel.clearSolution();
        mazeModel.solve();

        isSolutionVisible = true;

        reRenderCanvasPanel();
    }

    private void handleClearButton() {
        resetCanvasMazeModel();
        reRenderCanvasPanel();

        // System.out.println("Cleared");
        // System.out.println(System.identityHashCode(canvasPanel));
        // System.out.println(System.identityHashCode(mazeModel));
        // System.out.println(mazeModel);
    }

    private void handleRegenerateButton() {
        MazeGenerator genMaze = new MazeGenerator(mazeModel.getWidth(), mazeModel.getHeight(), mazeModel.getStartX(), mazeModel.getEndX());
        this.mazeModel = genMaze.getMaze();
//        System.out.println(mazeModel);
        reRenderCanvasPanel();
    }

    private void handleSolutionButton() {
        isSolutionVisible = !isSolutionVisible;

        reRenderCanvasPanel();

    }

    private void handleSaveButton() throws SQLException {
        mazeModel.clearSolution();
        MazeExporter cleanMaze = new MazeExporter(mazeModel, 32);
        mazeModel.solve();
        MazeExporter solvedMaze = new MazeExporter(mazeModel, 32);
        
        ImageIcon cleanImage = cleanMaze.getImageIcon();
        ImageIcon solvedImage = solvedMaze.getImageIcon();
        
        MazeBrowserData dbMazeBrowserData = MazeBrowserData.getInstance();
        dbMazeBrowserData.add(new MazeRecord("Test1", new User("Admin", "User", "tba", "tba"), mazeModel, cleanImage, solvedImage));
        window.dispose();
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
        public void actionPerformed(ActionEvent e) {
            Component source = (Component) e.getSource();
            if (source == saveBttn) {
                try {
                    handleSaveButton();
                } catch (SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } else if (source == clearBttn) {
                handleClearButton();
            } else if (source == checkBttn) {
                handleSanityCheckButton();
            } else if (source == solutionBttn) {
                handleSolutionButton();
            } else if (source == regenBttn) {
                handleRegenerateButton();
            }
        }
    }
    private class MazeButtonActionListener implements ActionListener, MouseListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        }

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
                }
            } else if (SwingUtilities.isRightMouseButton(e)) {
                if (mazeModel.getBlock(pressedCol, pressedRow).equals(Block.WALL)) {
                    setBlockButtonStyle(aBlockButton, Block.BLANK);
                    mazeModel.setBlock(Block.BLANK, pressedCol, pressedRow);
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
                MazeBrowserData.reSyncMazeRecords();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    public static MazeCanvas getInstance(MazeModel mazeModel) {
        isSolutionVisible = false;

        if (instance == null) {
           new MazeCanvas(mazeModel);
        } else {
            MazeCanvas.mazeModel = mazeModel;
            MazeCanvas.canvasPanel.setLayout(new GridLayout(mazeModel.getHeight(), mazeModel.getWidth()));
            instance.reRenderCanvasPanel();
            // window.repaint();
        }
        return instance;
     }
}