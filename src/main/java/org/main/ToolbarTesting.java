package org.main;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
//import java.awt.Dimension;

import javax.swing.*;

public class ToolbarTesting {
    public static void main(String[] args) throws InterruptedException {
        // TODO: Optimize the main and adding graphs
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){
            System.out.println("OS Not Detected");
        };

        DataInput.connect(DataInput.TEST);

        final JFrame frame = new JFrame("ToolBarTesting");

        frame.setLayout(new BorderLayout());
        frame.add(ToolbarPanel.instance, BorderLayout.SOUTH);
        frame.add(CanvasPanel.instance, BorderLayout.CENTER); // Can only show once datasets are built

        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent event) {
                CanvasPanel.instance.setupCanvasLayout();
            }
        });

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
