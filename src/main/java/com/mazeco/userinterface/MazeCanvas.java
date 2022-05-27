package com.mazeco.userinterface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;

import java.io.FileWriter;
import java.io.IOException;

import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;

import org.mariadb.jdbc.type.Point;

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
   
    private JPanel sidePanel;
    private JPanel mazeCanvasPanel;
    
    private MazeModel mazeModel;

    public MazeCanvas(MazeModel mazeModel) {
        this.mazeModel = mazeModel;

        initialiseSideMenu();
        
        render();

        initialiseWindow();
        
    }

    private void initialiseSideMenu() {
        sidePanel = new JPanel();
        sidePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        sidePanel.setLayout(new GridLayout(14, 1, 1, 8));
        sidePanel.add(logoBttn);
        sidePanel.add(startImgBttn);
        sidePanel.add(endImgBttn);
        sidePanel.add(sizeBttn);
        sidePanel.add(clearBttn);

        for (int i = 0; i < 7; i++) {
            JLabel blankPlaceHolder = new JLabel();
            sidePanel.add(blankPlaceHolder);
        }

        sidePanel.add(checkBttn);
        sidePanel.add(saveBttn);

        clearBttn.addActionListener(new SideMenuActionListener());
        saveBttn.addActionListener(new SideMenuActionListener());
    }

    private void initialiseWindow() {
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setMinimumSize(new Dimension(900, 800));
        window.setLayout(new BorderLayout());
        window.setResizable(true);
        window.add(sidePanel, BorderLayout.WEST);
        window.add(mazeCanvasPanel, BorderLayout.CENTER);
        window.pack();
    }

    public void render() {
        clearCanvas();
        mazeCanvasPanel = new JPanel(new GridLayout(mazeModel.getHeight(), mazeModel.getWidth()));

        for (int row = 0; row < mazeModel.getHeight(); row++) {
            for (int col = 0; col < mazeModel.getWidth(); col++) {
                JButton aBlockButton = new JButton();
                aBlockButton.addActionListener(new MazeButtonActionListener());
                aBlockButton.addMouseListener(new MazeButtonActionListener());
                aBlockButton.setBorderPainted(true);
                aBlockButton.setOpaque(true);
                Block aBlockModel = mazeModel.getBlock(col, row);
                if (aBlockModel.equals(Block.BLANK)) {
                    aBlockButton.setBackground(Color.WHITE);
                } else if (aBlockModel.equals(Block.START)) {
                    aBlockButton.setBackground(Color.RED);
                } else if (aBlockModel.equals(Block.END)) {
                    aBlockButton.setBackground(Color.GREEN);
                } else {
                    aBlockButton.setBackground(Color.BLACK);
                }
                aBlockButton.putClientProperty("position", new Point(col, row));
                aBlockButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                aBlockButton.setFocusPainted(false);

                mazeCanvasPanel.add(aBlockButton);
            }
        }
    }

    public void saveMaze(MazeModel maze) throws IOException {
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

    public void clearCanvas() {
        if (mazeCanvasPanel != null) {
            window.remove(mazeCanvasPanel);
            window.repaint();
        }
    }

    public void clearModel() {
        if (mazeModel != null)
            mazeModel = new MazeModel(mazeModel.getWidth(), mazeModel.getHeight());
    }


    @Override
    public void show() {
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private class MazeButtonActionListener implements ActionListener, MouseListener{
        @Override
        public void actionPerformed(ActionEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            
            if (SwingUtilities.isLeftMouseButton(e)){
                Point clickedPosition = (Point) button.getClientProperty("position");
                int pressedCol = (int) clickedPosition.getX();
                int pressedRow = (int) clickedPosition.getY();
                
                if (mazeModel.getBlock(pressedCol, pressedRow).equals(Block.BLANK)) {
                    button.setBackground(Color.BLACK);
                    button.setForeground(Color.WHITE);
                    mazeModel.setBlock(Block.WALL, pressedCol, pressedRow);
                } 
                System.out.println(mazeModel);
            }
            else if (SwingUtilities.isRightMouseButton(e)){
                Point clickedPosition = (Point) button.getClientProperty("position");
                int pressedCol = (int) clickedPosition.getX();
                int pressedRow = (int) clickedPosition.getY();
                
                if (mazeModel.getBlock(pressedCol, pressedRow).equals(Block.WALL)) {
                    button.setBackground(Color.WHITE);
                    button.setForeground(Color.BLACK);
                    mazeModel.setBlock(Block.BLANK, pressedCol, pressedRow);
                }
                
                System.out.println(mazeModel);
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)){
                JButton button = (JButton) e.getSource();
                Point clickedPosition = (Point) button.getClientProperty("position");
                int pressedCol = (int) clickedPosition.getX();
                int pressedRow = (int) clickedPosition.getY();
                
                if (mazeModel.getBlock(pressedCol, pressedRow).equals(Block.BLANK)) {
                    button.setBackground(Color.BLACK);
                    button.setForeground(Color.WHITE);
                    mazeModel.setBlock(Block.WALL, pressedCol, pressedRow);
                } else if (mazeModel.getBlock(pressedCol, pressedRow).equals(Block.WALL)) {
                    button.setBackground(Color.WHITE);
                    button.setForeground(Color.BLACK);
                    mazeModel.setBlock(Block.BLANK, pressedCol, pressedRow);
                }
            }
            
            System.out.println(mazeModel);
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

    private class SideMenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
           Component source = (Component) e.getSource();
            if (source == saveBttn) {
                try {
                    saveMaze(mazeModel);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                window.dispose();
                System.out.println("Saved");
            } else if (source == clearBttn) {
                clearModel();
                render();
                System.out.println("Cleared");
            } 
        }
    }
}