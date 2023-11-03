package org.main;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CanvasPanel extends JPanel {

    public static CanvasPanel instance = new CanvasPanel();

    private MigLayout layout;

    private boolean displayGraphs = false;

    private List<JPanel> primaryGraphs;
    private List<JPanel> secondaryGraphs;

    public Dimension canvasDimension = new Dimension(2, 4);

    private int index = 0;

    private StringBuilder columnConstraints;

    private CanvasPanel() {
        StringBuilder columnConstraints = new StringBuilder(Theme.graphPadding + "[][][][][]" + Theme.graphPadding);
        StringBuilder rowConstraints = new StringBuilder(Integer.toString(Theme.graphPadding));
        for (int i = 0; i < canvasDimension.getWidth() - 1; i++) {
            columnConstraints.append("[]").append(Theme.graphPadding);
        }
        for (int i = 0; i < canvasDimension.getHeight(); i++) {
            rowConstraints.append("[]").append(Theme.graphPadding);
        }

        layout = new MigLayout("fill", columnConstraints.toString(), rowConstraints.toString());

        primaryGraphs = new ArrayList<>(CanvasController.getPrimaryGraphs().size());
        secondaryGraphs = new ArrayList<>(CanvasController.getPrimaryGraphs().size());

        setLayout(layout);
        setBackground(Theme.canvasBackground);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        initializeGraphs();
        setupCanvasLayout();
    }

    public void setupCanvasLayout() {
        removeAll();
//        initializeGraphs();

//        int index = 0;

//        JPanel panel1 = new JPanel();
//        panel1.setLayout(new BorderLayout());
//
//        JPanel panel2 = new JPanel();
//        panel2.setLayout(new BorderLayout());
//
//        panel1.add(getDefaultTDCanvas(0), BorderLayout.CENTER);
//        panel2.add(getDefaultTDCanvas(1), BorderLayout.CENTER);

//        primaryGraphs.get(0).removeAll();
//        primaryGraphs.get(0).add(getDefaultTDCanvas(0), BorderLayout.CENTER);
//        primaryGraphs.get(1).removeAll();
//        primaryGraphs.get(1).add(getDefaultTDCanvas(1), BorderLayout.CENTER);
//        index++;

//        System.out.println(index);
        if (index == 0) {
            primaryGraphs.get(0).removeAll();
            primaryGraphs.get(0).add(getDefaultTDCanvas(0), BorderLayout.CENTER);
        }
        if (index == 1) {
            primaryGraphs.get(1).removeAll();
            primaryGraphs.get(1).add(getDefaultTDCanvas(1), BorderLayout.CENTER);
        }
        for (int i = 0; i < 4; i++) {
            Component prevComp = primaryGraphs.get(i).getComponent(0);
            primaryGraphs.get(i).removeAll();
            if (prevComp.getClass().equals(GLCanvas.class)) {
//                System.out.println("yeah its a canvas");
                primaryGraphs.get(i).add(replacementGraph(primaryGraphs.get(i), (GLCanvas) prevComp), BorderLayout.CENTER);
            } else {
                primaryGraphs.get(i).add(prevComp, BorderLayout.CENTER);
            }
            System.out.println(prevComp.getClass());
        }
        index++;

        add(primaryGraphs.get(0), "cell 0 0 5 1, grow");
        add(primaryGraphs.get(1), "cell 0 1 5 1, grow");
        add(primaryGraphs.get(2), "cell 0 2 5 1, grow");
        add(primaryGraphs.get(3), "cell 0 3 5 1, grow");

        add(secondaryGraphs.get(0), "cell 5 0, grow");
        add(secondaryGraphs.get(1), "cell 5 1, grow");
        add(secondaryGraphs.get(2), "cell 5 2, grow");
        add(secondaryGraphs.get(3), "cell 5 3, grow");
        repaint();
        revalidate();

//        if (index == 0) {
//            index++;
//            primaryGraphs.get(2).removeAll();
//            primaryGraphs.get(2).add(getDefaultTDCanvas(2), BorderLayout.CENTER);
//
//            setupCanvasLayout();
//        }
//        for (int column = 0; column < canvasDimension.getWidth(); column++) {
//            List<Component> components = column == 0 ? primaryGraphs : secondaryGraphs;
//            for (int row = 0; row < canvasDimension.getHeight(); row++) {
//                index = column == 0 ? row : (int) ((column - 1) * canvasDimension.getHeight()) + row;
//                String location = "cell " + column + " " + row;
//                System.out.println(index);
//                Component comp = components.get(index);
//                add(comp, location);
//            }
//        }
    }

    private void initializeGraphs() {
        List<List<JPanel>> graphColumns = new ArrayList<>(List.of(primaryGraphs, secondaryGraphs));

        for (int i = 0; i < canvasDimension.getWidth(); i++) {
            int column = i == 0 ? 0 : 1;
            List<? extends Graph> graphs = column == 0 ? CanvasController.getPrimaryGraphs() : CanvasController.getSecondaryGraphs();
            for (int j = 0; j < canvasDimension.getHeight(); j++) {
                int index = column == 0 ? j : (int) ((i - 1) * canvasDimension.getHeight()) + j;
                try {
                    if (graphs.get(index) == null) throw new IndexOutOfBoundsException("Null Graph");

                    System.out.println("Drawing somethin ---------------------------------------");
                    JPanel panel = new JPanel();
                    panel.setLayout(new BorderLayout());

                    final GLProfile profile = GLProfile.get(GLProfile.GL2);
                    GLCapabilities capabilities = new GLCapabilities(profile);

                    GLCanvas glCanvas = new GLCanvas(capabilities);
                    glCanvas.addGLEventListener(graphs.get(index));
                    glCanvas.addMouseListener(graphs.get(index));

                    Animator animator = new Animator(glCanvas);
                    animator.setUpdateFPSFrames(1, null);
                    animator.start();

                    glCanvas.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

                    panel.add(glCanvas, BorderLayout.CENTER);

                    graphColumns.get(column).add(panel);
                    System.out.println(index);
//                    System.out.println("Creating Known: " + "C=" + column + " | I=" + index);
                } catch (IndexOutOfBoundsException e1) {
                    try {
                        if (graphColumns.get(column).get(index) == null)
                            graphColumns.get(column).add(createNullButton(graphs, graphColumns, column, index));
                        else {
//                            System.out.println("Creating Uh this one...: " + "C=" + column + " | I=" + index);
                            Component previousComponent = graphColumns.get(column).get(index).getComponent(0);
                            graphColumns.get(column).get(index).removeAll();
                            graphColumns.get(column).get(index).add(previousComponent, BorderLayout.CENTER);
                        }
                    } catch (IndexOutOfBoundsException e2) {
                        graphColumns.get(column).add(createNullButton(graphs, graphColumns, column, index));
                    }
                }
            }
        }
    }

    private JPanel createNullButton(List<? extends Graph> graphs, List<List<JPanel>> graphColumns, int column, int index) {
//        System.out.println("Creating Null: " + "C=" + column + " | I=" + index);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JButton button = new JButton("+");
        button.addActionListener(event -> {
            graphColumns.get(column).get(index).removeAll();

            graphColumns.get(column).get(index).add(getDefaultTDCanvas(index), BorderLayout.CENTER);

            setupCanvasLayout();
        });
        panel.add(button, BorderLayout.CENTER);
        button.setFont(Theme.largeFont);
        button.setBackground(Theme.nullGraphColor);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        button.setBorder(BorderFactory.createEmptyBorder());

        panel.add(button);
        return panel;
    }

    public GLCanvas replacementGraph(JPanel container, GLCanvas oldCanvas) {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        GLCanvas replacement = new GLCanvas(capabilities);

        ((Graph) oldCanvas.getGLEventListener(0)).setPosition(container.getX(), container.getY(), container.getSize());

        replacement.addGLEventListener(oldCanvas.getGLEventListener(0));
        replacement.addMouseListener(oldCanvas.getMouseListeners()[0]);

        replacement.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        Animator animator = new Animator(replacement);
        animator.setUpdateFPSFrames(1, null);
        animator.start();

        return replacement;
    }

    public void toggleGraphs() {
        displayGraphs = !displayGraphs;
        if (displayGraphs) setupCanvasLayout();
        else removeAll();
    }

    public boolean areGraphsDisplayed() {
        return displayGraphs;
    }

    private GLCanvas getDefaultTDCanvas(int i) {
        OpenGLTimeDomain td = new OpenGLTimeDomain(0, 0, 0, 0);
        td.setDatasets(List.of(DatasetController.getDataset(i % DatasetController.getDatasets().size())));

//        CanvasController.addPrimaryGraph(CanvasController.TIME_DOMAIN, td);

        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        GLCanvas glCanvas = new GLCanvas(capabilities);
        glCanvas.addGLEventListener(td);
        glCanvas.addMouseListener(td);

        Animator animator = new Animator(glCanvas);
        animator.setUpdateFPSFrames(1, null);
        animator.start();

        glCanvas.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        return glCanvas;
    }
}
