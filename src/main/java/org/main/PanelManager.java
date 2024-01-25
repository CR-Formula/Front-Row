package org.main;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class PanelManager extends JFrame {
    public static PanelManager instance = new PanelManager("Front Row");
    private ToolbarPanel toolbarPanel;
    private Component centerPanel;
    private BorderLayout layout;

    public PanelManager(String name) {
        super(name);

        layout = new BorderLayout();
        setLayout(layout);
        toolbarPanel = ToolbarPanel.instance;
        add(toolbarPanel, BorderLayout.SOUTH);
    }

    public void addComponent(Component component, String location) {
        if (BorderLayout.CENTER.equals(location))
            centerPanel = component;
        add(component, location);
        repaint();
        revalidate();
    }

    public void removeComponent(Component component) {
        JPanel center = (JPanel) layout.getLayoutComponent(BorderLayout.CENTER);
        if (center.equals(component))
            centerPanel = null;
        remove(component);
        repaint();
        revalidate();
    }

    public void replaceComponent(Component component, String location) {
        if (BorderLayout.CENTER.equals(location)) {
            if (centerPanel != null)
                remove(centerPanel);
            add(component, location);
            centerPanel = component;
        }
        repaint();
        revalidate();
    }

}
