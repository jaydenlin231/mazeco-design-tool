package com.mazeco;

import com.mazeco.userinterface.UIOrchestrator;


public class App 
{
    /**
     * The entry point of the MazeCo Design Tool App. Launches the main menu of the Java Swing GUI
     * @param args
     * @author Wei-Chung Lin
     * @author Alexander Kim
     */
    public static void main( String[] args )
    {
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
