package com.mazeco.userinterface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.File;
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
    private static JLayeredPane layeredPane = new JLayeredPane();
    private static MazeModel mazeModel;

    private static boolean isSolutionVisible = false;

    private static MazeCanvas instance = null;

    private static String mazeName = null;
    private static User user = null;

    private static CanvasMode mode;
    private static UUID currentMazeID;

    private static ImageIcon startImageIcon;
    private static ImageIcon endImageIcon;
    private static ImageIcon logoIcon;

    private static JButton logoStartButton;

    private static JButton popupMenuCallerBlockButton;

    static JPopupMenu popupMenu = new JPopupMenu();
    static JMenuItem setStartItem = new JMenuItem("Set start");
    static JMenuItem setEndItem = new JMenuItem("Set end");
    static JMenuItem setLogo = new JMenuItem("Set logo");
    
    static int logoSetCount = 0;

    private MazeCanvas(MazeModel mazeModel, CanvasMode mode, String mazeName, User user) {
        if (instance != null)
            return;

        initSidePanelActionListeners();

        MazeCanvas.mazeModel = mazeModel;
        MazeCanvas.mazeName = mazeName;
        MazeCanvas.user = user;
        initialiseSidePanel(mode);
        MazeCanvas.canvasPanel = new JPanel(new GridLayout(mazeModel.getHeight(), mazeModel.getWidth()));
        MazeCanvas.canvasPanel.setPreferredSize(new Dimension(800, 800));
        renderCanvasPanelButtons();
        // renderLogoImage();
        initialiseWindow();
        // renderLogoImage();
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
            MazeCanvas.reRenderCanvasPanel();
        }
        resetCheckSolSaveButtons();
        logoSetCount = 0;
        
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
        startImgBttn.addActionListener(new SideMenuActionListener());
        endImgBttn.addActionListener(new SideMenuActionListener());
        logoBttn.addActionListener(new SideMenuActionListener());

        setStartItem.addActionListener(new PopupMenuListener());
        setEndItem.addActionListener(new PopupMenuListener());
        setLogo.addActionListener(new PopupMenuListener());

    }

    private void initialisePopupMenu(){
        
        popupMenu.add(setStartItem);
        popupMenu.add(setEndItem);
        popupMenu.add(setLogo);
    }

    private void initialiseSidePanel(CanvasMode mode) {
        sidePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        if (mazeModel.getLogo() != null) {
            logoBttn.setText("Remove Logo Image");
        } else {
            logoBttn.setText("Place Logo Image");
        }
        sidePanel.add(startImgBttn);
        if (mazeModel.getStartImage() != null) {
            startImgBttn.setText("Remove Start Image");
        } else {
            startImgBttn.setText("Place Start Image");
        }
        sidePanel.add(endImgBttn);
        if (mazeModel.getEndImage() != null) {
            endImgBttn.setText("Remove End Image");
        } else {
            endImgBttn.setText("Place End Image");
        }
        sidePanel.add(logoBttn);
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
        window.setMinimumSize(new Dimension(990, 830));
        window.setLayout(new BorderLayout());
        window.setResizable(false);
        
        layeredPane.add(canvasPanel, JLayeredPane.DEFAULT_LAYER);
        canvasPanel.setBounds(0, 0, 800, 800);
        initialisePopupMenu();
        window.add(popupMenu);
        window.add(layeredPane, BorderLayout.CENTER);
        window.add(sidePanel, BorderLayout.WEST);
//        window.add(canvasPanel, BorderLayout.CENTER);
        window.pack();

    }

    private void reRenderSidePanel(CanvasMode mode){
        removeAllAndRepaintSidePanel();
        initialiseSidePanel(mode);
        sidePanel.repaint();
        window.repaint();
        window.pack();
    }

    private static void reRenderCanvasPanel() {
        removeAllAndRepaintCanvasPanel();
        renderCanvasPanelButtons();

        canvasPanel.repaint();
        window.repaint();
        window.pack();
    }

    private static void reRenderLogoImage() {
        layeredPane.removeAll();
        layeredPane.add(canvasPanel, JLayeredPane.DEFAULT_LAYER);
        renderLogoImage();

        layeredPane.repaint();
        window.repaint();
        window.pack();
    }

    private static void renderLogoImage() {
        reRenderCanvasPanel();
        if (mazeModel.getLogo() != null) {
            int LogoStartX = logoStartButton.getX();
            int LogoStartY = logoStartButton.getY();
            System.out.println(logoStartButton.getX());
            System.out.println(logoStartButton.getY());
            System.out.println(logoStartButton.getSize());
            int LogoWidth = (mazeModel.getEndLogoPoint().x - mazeModel.getStartLogoPoint().x + 1) * (int)logoStartButton.getSize().getWidth();
            int LogoHeight = (mazeModel.getEndLogoPoint().y - mazeModel.getStartLogoPoint().y + 1) * (int)logoStartButton.getSize().getHeight();

            logoIcon = new ImageIcon(mazeModel.getLogo());
            Image img = logoIcon.getImage();
            Image newImg = img.getScaledInstance(LogoWidth, LogoHeight, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(newImg);

            JLabel label2 = new JLabel();
            label2.setIcon(logoIcon);
            label2.setBounds(LogoStartX, LogoStartY, LogoWidth, LogoHeight);
            layeredPane.add(label2, JLayeredPane.POPUP_LAYER);
        }
    }

    private static void renderCanvasPanelButtons() {
        for (int row = 0; row < mazeModel.getHeight(); row++) {
            for (int col = 0; col < mazeModel.getWidth(); col++) {
                JButton aBlockButton = new JButton();

                aBlockButton.addMouseListener(new MazeButtonActionListener());

                aBlockButton.setBorderPainted(true);
                aBlockButton.setOpaque(true);

                Block aBlockModel = mazeModel.getBlock(col, row);

                setBlockButtonStyle(aBlockButton, aBlockModel);

                aBlockButton.putClientProperty("position", new Point(col, row));
                if(mazeModel.getLogo() != null && mazeModel.getStartLogoPoint().x == col && mazeModel.getStartLogoPoint().y == row){
                    logoStartButton = aBlockButton;
                }
                aBlockButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                aBlockButton.setFocusPainted(false);

                canvasPanel.add(aBlockButton);
            }
        }
    }


    private static void removeAllAndRepaintCanvasPanel() {
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

    private static void setBlockButtonStyle(JButton aBlockButton, Block aBlock) {
        switch (aBlock) {
            case BLANK:
                aBlockButton.setBackground(Color.WHITE);
                break;

            case WALL:
                aBlockButton.setBackground(Color.BLACK);
                break;

            case LOGO:
                aBlockButton.setBackground(Color.PINK);
                break;

            case START:
                if (mazeModel.getStartImage() != null) {
                    startImageIcon = new ImageIcon(mazeModel.getStartImage());
                    Image img = startImageIcon.getImage();
                    Image newImg = img.getScaledInstance(800 / mazeModel.getWidth(), 800 / mazeModel.getWidth(), Image.SCALE_SMOOTH);
                    startImageIcon = new ImageIcon(newImg);
                    aBlockButton.setIcon(startImageIcon);
                } else
                    aBlockButton.setBackground(Color.RED);
                break;
                
            case END:
                if (mazeModel.getEndImage() != null) {
                    endImageIcon = new ImageIcon(mazeModel.getEndImage());
                    Image img = endImageIcon.getImage();
                    Image newImg = img.getScaledInstance(800 / mazeModel.getWidth(), 800 / mazeModel.getWidth(), Image.SCALE_SMOOTH);
                    endImageIcon = new ImageIcon(newImg);
                    aBlockButton.setIcon(endImageIcon);
                } else
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
        mazeModel.setLogoImage(null);
        reRenderCanvasPanel();

        reRenderLogoImage();
        resetCheckSolSaveButtons();
        logoSetCount = 0;
        logoBttn.setText("Place Logo");
        startImgBttn.setText("Place Start Image");
        endImgBttn.setText("Place End Image");
    }

    private void handleRegenerateButton() {
        MazeCanvas.mazeModel = MazeGenerator.generateMaze(mazeModel.getWidth(), mazeModel.getHeight(), mazeModel.getStartX(), mazeModel.getEndX(), mazeModel.getLogo(), mazeModel.getStartImage(), mazeModel.getEndImage());
        reRenderCanvasPanel();
        reRenderLogoImage();
        resetCheckSolSaveButtons();
        logoSetCount = 0;
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

    private void openImageBrowser(JButton source) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Images", "jpg", "JPEG", "png");
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int option = fileChooser.showOpenDialog(null);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (source == startImgBttn) {
                mazeModel.setStartImage(file.getAbsolutePath());
                reRenderCanvasPanel();
                startImgBttn.setText("Remove Start Image");
            }
            if (source == endImgBttn) {
                mazeModel.setEndImage(file.getAbsolutePath());
                reRenderCanvasPanel();
                endImgBttn.setText("Remove End Image");
            }
            if (source == logoBttn) {
                mazeModel.setLogoImage(file.getAbsolutePath());
                if(mazeModel.getStartLogoPoint() == null)    
                    mazeModel.prepForLogo();
                reRenderLogoImage();
                logoBttn.setText("Remove Logo");
            }
        }
    }

    @Override
    public void show() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                window.setLocationRelativeTo(null);
                window.setVisible(true);
                reRenderLogoImage();
            }
        });
    }

    private class SideMenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            Component source = (Component) e.getSource();
            try {
//                if (source == regenBttn || source == clearBttn){
//                    int confirmation = JOptionPane.showConfirmDialog(window,
//                                                                 "Do you wish to clear the drawing canvas? Your current changes will be lost.",
//                                                                 "Warning",
//                                                                 JOptionPane.YES_NO_OPTION,
//                                                                 JOptionPane.WARNING_MESSAGE);
//
//                    if(confirmation == JOptionPane.NO_OPTION || confirmation == JOptionPane.CLOSED_OPTION)
//                        return;
//                }
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
                } else if (source == startImgBttn) {
                    handleStartImageButton(source);
                } else if (source == endImgBttn) {
                    handleEndImageButton(source);
                } else if (source == logoBttn) {
                    handleLogoImageButton(source);
                }
            } catch (SQLException exception) {
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (UnsolvableMazeException exception) {
                JOptionPane.showMessageDialog(window, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                saveBttn.setEnabled(false);
                solutionBttn.setEnabled(false);
            } catch (Exception exception) {
                exception.printStackTrace();
                JOptionPane.showMessageDialog(window, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void handleLogoImageButton(Component source) {
            if(mazeModel.getStartLogoPoint() != null)
                mazeModel.removeLogo();
            if (mazeModel.getLogo() != null) {
                mazeModel.setLogoImage(null);
                logoBttn.setText("Place Logo");
            } else if (mazeModel.getLogo() == null) {
                openImageBrowser((JButton) source);
            }
            reRenderLogoImage();
            resetCheckSolSaveButtons();
            logoSetCount = 0;
        }

        private void handleEndImageButton(Component source) {
            if (mazeModel.getEndImage() != null) {
                mazeModel.setEndImage(null);
                reRenderCanvasPanel();
                endImgBttn.setText("Place End Image");
            } else if (mazeModel.getEndImage() == null) {
                openImageBrowser((JButton) source);
            }
        }

        private void handleStartImageButton(Component source) {
            if (mazeModel.getStartImage() != null) {
                mazeModel.setStartImage(null);
                reRenderCanvasPanel();
                startImgBttn.setText("Place Start Image");
            } else if (mazeModel.getStartImage() == null) {
                openImageBrowser((JButton) source);
            }
        }
    }
    private static class MazeButtonActionListener implements MouseListener {
        @Override
        public void mouseEntered(MouseEvent e) {
            JButton aBlockButton = (JButton) e.getSource();
            Point clickedPosition = (Point) aBlockButton.getClientProperty("position");
            int pressedCol = (int) clickedPosition.getX();
            int pressedRow = (int) clickedPosition.getY();
            popupMenu.setVisible(false);
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

            if (SwingUtilities.isRightMouseButton(e)){
                handlePopupMenu(e);
            }
        }

        private void handlePopupMenu(MouseEvent e) {
            popupMenuCallerBlockButton = (JButton) e.getSource();
            Point clickedPosition = (Point) popupMenuCallerBlockButton.getClientProperty("position");
            int pressedCol = (int) clickedPosition.getX();
            int pressedRow = (int) clickedPosition.getY();

            if(mazeModel.getLogo() == null||mazeModel.getBlock(pressedCol, pressedRow) == Block.START||mazeModel.getBlock(pressedCol, pressedRow) == Block.END||mazeModel.getBlock(pressedCol, pressedRow) == Block.PATH)
                setLogo.setEnabled(false);
            else
                setLogo.setEnabled(true);

            popupMenu.show(popupMenuCallerBlockButton, e.getX(), e.getY());
        }


        // Not Used
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

    private static class PopupMenuListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            Point clickedPosition = (Point) popupMenuCallerBlockButton.getClientProperty("position");
            int pressedCol = (int) clickedPosition.getX();
            int pressedRow = (int) clickedPosition.getY();
            if(mazeModel.getBlock(pressedCol, pressedRow) == Block.LOGO)
                return;

            if(e.getSource() == setStartItem){
                handlePopupSetStart(pressedCol, pressedRow);
            }
            else if(e.getSource() == setEndItem){
                handlePopupSetEnd(pressedCol, pressedRow);
            }
            else if(e.getSource() == setLogo){
                handlePopupSetLogo(clickedPosition);
            }
            mazeModel.clearSolution();
            reRenderCanvasPanel();
            resetCheckSolSaveButtons();
        }

        private void handlePopupSetLogo(Point clickedPosition) {
            if(logoSetCount == 0){
                if(mazeModel.getStartLogoPoint() != null && mazeModel.getEndLogoPoint() != null)
                    mazeModel.removeLogo();
                    
                mazeModel.setLogoArea(clickedPosition, clickedPosition);
                logoSetCount ++;
            }
            else if(logoSetCount == 1){
                mazeModel.setLogoArea(mazeModel.getStartLogoPoint(), clickedPosition);
                logoSetCount = 0;
            }
            reRenderLogoImage();
        }

        private void handlePopupSetEnd(int pressedCol, int pressedRow) {
            mazeModel.clearSolution();
            mazeModel.setBlock(Block.WALL, mazeModel.getEndPosition().x, mazeModel.getEndPosition().y);
            mazeModel.setBlock(Block.END, pressedCol, pressedRow);
        }

        private void handlePopupSetStart(int pressedCol, int pressedRow) {
            mazeModel.clearSolution();
            mazeModel.setBlock(Block.WALL, mazeModel.getStartPosition().x, mazeModel.getStartPosition().y);
            mazeModel.setBlock(Block.START, pressedCol, pressedRow);
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