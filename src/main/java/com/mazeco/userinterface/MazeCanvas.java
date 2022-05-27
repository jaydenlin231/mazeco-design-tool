package com.mazeco.userinterface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.io.FileWriter;
import java.io.IOException;

import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;

public class MazeCanvas implements IUserInterface {
    private final String TITLE = "Editor";
    private final JFrame window = new JFrame(TITLE);

    private final JButton saveBttn = new JButton("Save");
    private final JButton logoBttn = new JButton("Place Logo");
    private final JButton startImgBttn = new JButton("Place Start Image");
    private final JButton endImgBttn = new JButton("Place End Image");
    private final JButton sizeBttn = new JButton("Change Maze Size");
    private final JButton checkBttn = new JButton("Check Maze");
    private final JButton clearBttn = new JButton("Clear Maze");
    private final JButton solutionBttn = new JButton("Toggle Solution");

    private final JPanel sidePanel = new JPanel(new GridLayout(14, 1, 1, 8));
    private final JPanel canvasPanel;
    private final MazeModel mazeModel;

    private boolean isSolutionVisible = false;

    public MazeCanvas(MazeModel mazeModel) {
        this.mazeModel = mazeModel;

        initialiseSidePanel();

        canvasPanel = new JPanel(new GridLayout(mazeModel.getHeight(), mazeModel.getWidth()));

        renderCanvasPanelButtons();

        initialiseWindow();
    }

    private void initialiseSidePanel() {
        sidePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        sidePanel.add(logoBttn);
        sidePanel.add(startImgBttn);
        sidePanel.add(endImgBttn);
        sidePanel.add(sizeBttn);
        sidePanel.add(clearBttn);

        for (int i = 0; i < 6; i++) {
            JLabel blankPlaceHolder = new JLabel();
            sidePanel.add(blankPlaceHolder);
        }

        sidePanel.add(checkBttn);
        sidePanel.add(solutionBttn);
        sidePanel.add(saveBttn);

        clearBttn.addActionListener(new SideMenuActionListener());
        checkBttn.addActionListener(new SideMenuActionListener());
        saveBttn.addActionListener(new SideMenuActionListener());
        solutionBttn.addActionListener(new SideMenuActionListener());
        
    }

    private void initialiseWindow() {
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setMinimumSize(new Dimension(900, 800));
        window.setLayout(new BorderLayout());
        window.setResizable(true);

        window.add(sidePanel, BorderLayout.WEST);
        window.add(canvasPanel, BorderLayout.CENTER);
        window.pack();
    }

    private void reRenderCanvasPanel(){
        removeAllAndRepaintCanvasPanel();
        renderCanvasPanelButtons();
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

    private void handleSolutionButton() {
        isSolutionVisible = !isSolutionVisible;

        reRenderCanvasPanel();

    }

    private void handleSaveButton() {
        try {
            saveMaze(mazeModel);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        window.dispose();
        System.out.println("Saved");
    }

    @Override
    public void show() {
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private class SideMenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Component source = (Component) e.getSource();
            if (source == saveBttn) {
                handleSaveButton();
            } else if (source == clearBttn) {
                handleClearButton();
            } else if (source == checkBttn) {
                handleSanityCheckButton();
            } else if (source == solutionBttn) {
                handleSolutionButton();
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
}