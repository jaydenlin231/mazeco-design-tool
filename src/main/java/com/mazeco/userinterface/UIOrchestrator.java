package com.mazeco.userinterface;

import javax.swing.*;

/**
 * Invokes the GUI application.
 */
public class UIOrchestrator {

    public void run(){
        JFrame.setDefaultLookAndFeelDecorated(false);
        MainMenu.getInstance().show();
    }
}
