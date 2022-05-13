package com.mazeco.userinterface;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BrowseWindow implements IUserInterface{
    private static final String TITLE = "Browse";
    private static final JFrame window = new JFrame(TITLE);

    public BrowseWindow(){
        window.setPreferredSize(new Dimension(1000, 800));
        window.setMinimumSize(new Dimension(1000, 800));
        window.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // Centre the window
        window.setLocationRelativeTo(null);
    }


    @Override
    public void show() {
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        System.out.println(window);
    }
    
}
