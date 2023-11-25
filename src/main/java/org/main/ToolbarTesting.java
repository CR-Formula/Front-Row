package org.main;

import java.awt.*;
//import java.awt.Dimension;

import javax.swing.*;

public class ToolbarTesting {
    public static void main(String[] args) throws InterruptedException {
        // TODO: Optimize the main
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){
            System.out.println("OS Not Detected");
        };

//        DataInput.connect(DataInput.TEST);

        final JFrame frame = PanelManager.instance;

        int width = 192 * 2;
        int height = 144 * 2;
        Dimension size = new Dimension(width, height);
        frame.setSize(size);
        Dimension resolution144p = new Dimension(192, 144);
        frame.setMinimumSize(resolution144p);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
    }
}
