package org.main;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import org.main.InitialGraphPanel.GraphType;

public class CanvasPanel extends JPanel {
    public static CanvasPanel instance = new CanvasPanel();

    private MigLayout layout;

    private boolean displayGraphs = false;
    private List<JLayeredPane> primaryGraphs;
    private List<JLayeredPane> secondaryGraphs;

    public Dimension canvasDimension = new Dimension(2, 4);

    final GLProfile profile = GLProfile.get(GLProfile.GL2);
    GLCapabilities capabilities = new GLCapabilities(profile);

    private boolean runningSetup = false;


    private CanvasPanel() {
        updateLayout();

        primaryGraphs = new ArrayList<>();
        for (int i = 0; i < canvasDimension.getHeight(); i++)
            primaryGraphs.add(new JLayeredPane());

        secondaryGraphs = new ArrayList<>();
        for (int i = 0; i < (int) (canvasDimension.getHeight() * (canvasDimension.getWidth() - 1)); i++)
            secondaryGraphs.add(new JLayeredPane());

        setBackground(Theme.canvasBackground);

        initializePanels();
        setupCanvasLayout();
    }

    private void updateLayout() {
        String columnConstraints = generateConstraints(Theme.graphPadding + "[][][][][]" + Theme.graphPadding, canvasDimension.getWidth()-1);
        String rowConstraints = generateConstraints(Integer.toString(Theme.graphPadding), canvasDimension.getHeight());

        layout = new MigLayout("fill", columnConstraints, rowConstraints);
        setLayout(layout);
    }

    private String generateConstraints(String padding, double dimension) {
        StringBuilder constraints = new StringBuilder(padding);
        for (int i = 0; i < dimension; i++) {
            constraints.append("[]").append(Theme.graphPadding);
        }
        return constraints.toString();
    }

    void initializePanels() {
        for (int column = 0; column < canvasDimension.getWidth();column++) {
            for (int row = 0; row < canvasDimension.getHeight(); row++) {
                JLayeredPane currentPanel = (column == 0 ? primaryGraphs.get(row) : secondaryGraphs.get((int) (((column - 1) * canvasDimension.getHeight()) + row)));
                createBlankGraph(currentPanel);
            }
        }
    }

    private void createBlankGraph(JLayeredPane container) {
        container.setLayout(new BorderLayout());
        JButton button = new JButton("+");
        button.setFocusPainted(false);
        button.addActionListener(event -> {
            int pIndex = primaryGraphs.indexOf(container);
            int sIndex = secondaryGraphs.indexOf(container);
            if (pIndex == -1 && sIndex == -1)
                return;

            GraphType type = pIndex != -1 && sIndex == -1 ? GraphType.PRIMARY : GraphType.SECONDARY;
            createGraph(container, type);
        });
        button.setFont(Theme.largeFont);
        button.setBackground(Theme.blankGraphColor);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        button.setBorder(BorderFactory.createEmptyBorder());

        container.add(button, Theme.BaseLayer);
    }

    private void createGraph(JLayeredPane container, GraphType type) {
        GLCanvas graph = new GLCanvas(capabilities);
        new InitialGraphPanel(container, graph, type);
    }

    private void removeGraph(JLayeredPane container){
        container.removeAll();
        createBlankGraph(container);
        setupCanvasLayout();
    }

    public void setupCanvasLayout() {
        if (runningSetup) return;
        runningSetup = true;
        removeAll();

        for (int i = 0; i < canvasDimension.getWidth(); i++) {
            int column = i == 0 ? i : i + 4;
            List<JLayeredPane> componentList = i == 0 ? primaryGraphs : secondaryGraphs;
            for (int j = 0; j < canvasDimension.getHeight(); j++) {
                int index = i == 0 ? j : (int) ((i - 1) * canvasDimension.getHeight()) + j;

                JLayeredPane container = componentList.get(index);

                boolean activeGraph = false;
                GLCanvas graphCanvas = null;
                for(Component component : container.getComponents()){
                    if(component.getClass() == GLCanvas.class){
                        graphCanvas = (GLCanvas) component;
                        activeGraph = true;
                    }
                }
                if(activeGraph){
                    container.removeAll();

//                    ((Graph) graphCanvas.getGLEventListener(0)).setPosition(container.getX(), container.getY(), container.getSize());

                    GLCanvas replacement = new GLCanvas(capabilities);
                    replacement.addGLEventListener(graphCanvas.getGLEventListener(0));
                    replacement.addMouseListener(graphCanvas.getMouseListeners()[0]);

                    Animator animator = new Animator(replacement);
                    animator.setUpdateFPSFrames(1, null);
                    animator.start();

                    container.add(newRemoveButton(container));
                    container.add(replacement);
                }

                String location = "cell " + column + " " + j + ", grow";
                if (column == 0)
                    location = "cell " + column + " " + j + " 5 1 , grow";

                add(container, location);
            }
        }
        repaint();
        revalidate();

        runningSetup = false;
    }

    private JButton newRemoveButton(JLayeredPane container) {
        JButton removeButton = new JButton("X");
        removeButton.setBackground(Color.BLACK);
        removeButton.setForeground(Color.WHITE);
        removeButton.setOpaque(true);
        removeButton.setBorderPainted(false);
        removeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                removeButton.setBackground(Color.GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                removeButton.setBackground(Color.BLACK);
            }
        });
        removeButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        int buttonSize = container.getHeight() / 8;
        removeButton.setSize(buttonSize, buttonSize);
        removeButton.setBounds(container.getWidth() - (int) (buttonSize * 1.5), 0, removeButton.getWidth(), removeButton.getHeight());
        removeButton.addActionListener(event -> removeGraph(container));
        return removeButton;
    }

    public void setCanvasDimension(int height) {
        if (height > canvasDimension.getHeight()) {
            for (int i = 0; i < height - canvasDimension.getHeight(); i++) {
                primaryGraphs.add(new JLayeredPane());
                secondaryGraphs.add(new JLayeredPane());
            }
        }
        canvasDimension = new Dimension((int) canvasDimension.getWidth(), height);
        updateLayout();
        initializePanels();
        setupCanvasLayout();
    }
}
