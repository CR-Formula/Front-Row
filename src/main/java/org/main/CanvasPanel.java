package org.main;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.Animator;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CanvasPanel extends JPanel {
    /* TODO: Create default which will display one add button,
     *     then add another below it which is thinner add button below it.
     *     Creation panel needed to select which Graph is wanted as well!!!
    */
    public static CanvasPanel instance = new CanvasPanel();

    private MigLayout layout;

    private boolean displayGraphs = false;

    private List<JPanel> primaryGraphs;
    private List<JPanel> secondaryGraphs;

    public Dimension canvasDimension = new Dimension(2, 4);

    final GLProfile profile = GLProfile.get(GLProfile.GL2);
    GLCapabilities capabilities = new GLCapabilities(profile);

    private boolean runningSetup = false;

    private CanvasPanel() {
        updateLayout();

        primaryGraphs = new ArrayList<>(CanvasController.getPrimaryGraphs().size());
        secondaryGraphs = new ArrayList<>(CanvasController.getPrimaryGraphs().size());

        setBackground(Theme.canvasBackground);

        initializeGraphs();
        setupCanvasLayout();
    }

    public void setCanvasDimension(int height) {
        canvasDimension = new Dimension((int) canvasDimension.getWidth(), height);
        updateLayout();
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


    public void setupCanvasLayout() {
        if (runningSetup) return;
        runningSetup = true;
        removeAll();

        for (int i = 0; i < canvasDimension.getWidth(); i++) {
            int column = i == 0 ? i : i + 4;
            List<JPanel> componentList = i == 0 ? primaryGraphs : secondaryGraphs;
            for (int j = 0; j < canvasDimension.getHeight(); j++) {
                int index = i == 0 ? j : (int) ((i - 1) * canvasDimension.getHeight()) + j;

                Component component = componentList.get(index).getComponent(0);
                componentList.get(index).removeAll();
                if (component.getClass().equals(GLJPanel.class)) {
                    componentList.get(index).add(replacementGraph(componentList.get(index), (GLJPanel) component), BorderLayout.CENTER);
                } else {
                    componentList.get(index).add(component, BorderLayout.CENTER);
                }
                String location = "cell " + column + " " + j + ", grow";
                if (column == 0)
                    location = "cell " + column + " " + j + " 5 1 , grow";

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
                    JPanel panel = new JPanel();
                    panel.setLayout(new BorderLayout());

                    GLJPanel glJPanel = new GLJPanel(capabilities);
                    glJPanel.addGLEventListener(graphs.get(index));
                    glJPanel.addMouseListener(graphs.get(index));

                    Animator animator = new Animator(glJPanel);
                    animator.setUpdateFPSFrames(1, null);
                    animator.start();

                    glJPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

                    panel.add(glJPanel, BorderLayout.CENTER);

                    graphColumns.get(column).add(panel);
                } catch (IndexOutOfBoundsException e1) {
                    try {
                        if (graphColumns.get(column).get(index) == null)
                            graphColumns.get(column).add(createNullButton(graphs, graphColumns, column, index));
                        else {
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
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JButton button = new JButton("+");
        button.setFocusPainted(false);
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

    public GLJPanel replacementGraph(JPanel container, GLJPanel oldCanvas) {
        GLJPanel replacement = new GLJPanel(capabilities);

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

    private GLJPanel getDefaultTDCanvas(int i) {
        OpenGLTimeDomain td = new OpenGLTimeDomain(0, 0, 0, 0);
        td.setDatasets(List.of(DatasetController.getDataset(i % DatasetController.getDatasets().size())));
//        td.toggleAutoDetectMaxMin();
//        CanvasController.addPrimaryGraph(CanvasController.TIME_DOMAIN, td);

        GLJPanel glJPanel = new GLJPanel(capabilities);
        glJPanel.addGLEventListener(td);
        glJPanel.addMouseListener(td);

        Animator animator = new Animator(glJPanel);
        animator.setUpdateFPSFrames(1, null);
        animator.start();

        glJPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        return glJPanel;
    }

    private GLJPanel getDefaultDialCanvas(int i) {
        OpenGLDial dial = new OpenGLDial(0, 0, 0, 0);
        dial.setDataset(DatasetController.getDataset(i % DatasetController.getDatasets().size()));

        dial.toggleAutoDetectMaxMin();

//        CanvasController.addPrimaryGraph(CanvasController.TIME_DOMAIN, td);

        GLJPanel glJPanel = new GLJPanel(capabilities);
        glJPanel.addGLEventListener(dial);
        glJPanel.addMouseListener(dial);

        Animator animator = new Animator(glJPanel);
        animator.setUpdateFPSFrames(1, null);
        animator.start();

        glJPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        return glJPanel;
    }
}
