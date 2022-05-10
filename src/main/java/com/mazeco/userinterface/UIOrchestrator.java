package com.mazeco.userinterface;

import java.awt.*;
import javax.swing.*;

public class UIOrchestrator {
    private MainMenu mainMenu = new MainMenu();

    public void run(){
        JFrame.setDefaultLookAndFeelDecorated(false);
        
        mainMenu.show();
    }

}
