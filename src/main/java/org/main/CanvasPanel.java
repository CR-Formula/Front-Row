package org.main;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
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

    private StringBuilder columnConstraints;

    private boolean runningSetup = false;

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
        if (runningSetup) return;
        runningSetup = true;
        removeAll();

//        for (int i = 0; i < 4; i++) {
//            Component prevComp = primaryGraphs.get(i).getComponent(0);
//            primaryGraphs.get(i).removeAll();
//            if (prevComp.getClass().equals(GLCanvas.class)) {
//                primaryGraphs.get(i).add(replacementGraph(primaryGraphs.get(i), (GLCanvas) prevComp), BorderLayout.CENTER);
//            } else {
//                primaryGraphs.get(i).add(prevComp, BorderLayout.CENTER);
//            }
//            System.out.println(prevComp.getClass());
//        }
//
//        add(primaryGraphs.get(0), "cell 0 0 5 1, grow");
//        add(primaryGraphs.get(1), "cell 0 1 5 1, grow");
//        add(primaryGraphs.get(2), "cell 0 2 5 1, grow");
//        add(primaryGraphs.get(3), "cell 0 3 5 1, grow");
//
//        add(secondaryGraphs.get(0), "cell 5 0, grow");
//        add(secondaryGraphs.get(1), "cell 5 1, grow");
//        add(secondaryGraphs.get(2), "cell 5 2, grow");
//        add(secondaryGraphs.get(3), "cell 5 3, grow");
//        repaint();
//        revalidate();

        for (int i = 0; i < canvasDimension.getWidth(); i++) {
            int column = i == 0 ? i : i + 4;
            List<JPanel> componentList = i == 0 ? primaryGraphs : secondaryGraphs;
            for (int j = 0; j < canvasDimension.getHeight(); j++) {
                int index = i == 0 ? j : (int) ((i - 1) * canvasDimension.getHeight()) + j;
                System.out.println(i + " | " + index + " : " + componentList.get(index).getComponentCount());

                Component component = componentList.get(index).getComponent(0);
                componentList.get(index).removeAll();
                if (component.getClass().equals(GLCanvas.class)) {
                    componentList.get(index).add(replacementGraph(componentList.get(index), (GLCanvas) component), BorderLayout.CENTER);
                } else {
                    componentList.get(index).add(component, BorderLayout.CENTER);
                }
                String location = "cell " + column + " " + j + ", grow";
                if (column == 0)
                    location = "cell " + column + " " + j + " 5 1 , grow";
                System.out.println(location);
                System.out.println(i + " | " + index + " : " + componentList.get(index).getComponent(0).getClass());
                System.out.println("----------");

                add(componentList.get(index), location);
            }
        }
        repaint();
        revalidate();

        runningSetup = false;
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

            graphColumns.get(column).get(index).add(column == 0 ? getDefaultTDCanvas(index) : getDefaultDialCanvas(index), BorderLayout.CENTER);

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

    private GLCanvas getDefaultDialCanvas(int i) {
        OpenGLDial dial = new OpenGLDial(0, 0, 0, 0);
        dial.setDataset(DatasetController.getDataset(i % DatasetController.getDatasets().size()));

        dial.toggleAutoDetectMaxMin();

//        CanvasController.addPrimaryGraph(CanvasController.TIME_DOMAIN, td);

        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        GLCanvas glCanvas = new GLCanvas(capabilities);
        glCanvas.addGLEventListener(dial);
        glCanvas.addMouseListener(dial);

        Animator animator = new Animator(glCanvas);
        animator.setUpdateFPSFrames(1, null);
        animator.start();

        glCanvas.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        return glCanvas;
    }
}
