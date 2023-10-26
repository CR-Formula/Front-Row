package org.main;

import java.awt.*;
//import java.awt.Dimension;

import javax.swing.*;

public class ToolBarTesting {
    public static void main(String[] args) throws InterruptedException {
        // TODO: Optimize the main and adding graphs
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){
            System.out.println("OS Not Detected");
        };

        final JFrame frame = new JFrame("ToolBarTesting");

        frame.setLayout(new BorderLayout());
        frame.add(ToolBarPanel.instance, BorderLayout.SOUTH);

        int width = 192 * 2;
        int height = 144 * 2;
        Dimension size = new Dimension(width, height);
        frame.setSize(size);
        Dimension resolution144p = new Dimension(192, 144);
        frame.setMinimumSize(resolution144p);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);



    }
}
