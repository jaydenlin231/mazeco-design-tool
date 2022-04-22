package com.mazeco;

import com.mazeco.models.Block;
import com.mazeco.models.BlockType;
import com.mazeco.models.MazeModel;
import com.mazeco.userinterface.UIOrchestrator;


public class App 
{
    public static void main( String[] args )
    {

        MazeModel aMazeModel = new MazeModel(5, 5);

        System.out.println(aMazeModel.toString());
        System.out.println("\n");
        aMazeModel.setBlockType(BlockType.WALL, 0, 4);
        System.out.println(aMazeModel.toString());

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIOrchestrator gui = new UIOrchestrator();
                    gui.run();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
