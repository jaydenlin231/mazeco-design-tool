package com.mazeco.userinterface;

import javax.swing.*;

public class UIOrchestrator {

    public void run(){
        JFrame.setDefaultLookAndFeelDecorated(false);
        MainMenu.getInstance().show();
    }
}
