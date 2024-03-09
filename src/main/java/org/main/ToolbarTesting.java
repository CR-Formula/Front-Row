package org.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
//import java.awt.Dimension;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ToolbarTesting {
    private static final int DELAY = 500;
    public static void main(String[] args) throws InterruptedException, IOException {
        // TODO: Optimize the main
        System.setProperty("--add-exports java.base/java.lang", "ALL-UNNAMED");
        System.setProperty("--add-exports java.desktop/sun.awt", "ALL-UNNAMED");
        System.setProperty("--add-exports java.desktop/sun.java2d", "ALL-UNNAMED");
        System.setProperty("-Dsun.java2d.opengl", "true");
        System.setProperty("-Djogl.GLContext.shareContextCache", "false");
        System.setProperty("-Dsun.java2d.noddraw", "true");
        System.setProperty("-Djogl.GLSharedContextSetter", "full");
        System.setProperty("-Djogl.GLContext.numContentHandles", "32");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e){
            System.out.println("OS Not Detected");
        };

        DataInput.connect(DataInput.TEST);

        final JFrame frame = PanelManager.instance;

//        frame.setContentPane(new JLabel(new ImageIcon(ImageIO.read(new File("src/main/resources/Cy-Racing.png")))));

        int width = 192 * 2;
        int height = 144 * 2;
        Dimension size = new Dimension(width, height);
        frame.setSize(size);
        Dimension resolution144p = new Dimension(192, 144);
        frame.setMinimumSize(resolution144p);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

        frame.addComponentListener(new ComponentAdapter() {
            private Timer timer;
            public void componentResized(ComponentEvent e) {
                if (timer != null && timer.isRunning()) {
                    timer.stop();
                }
                timer = new Timer(DELAY, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        CanvasPanel.instance.setupCanvasLayout();
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        });
    }
}
