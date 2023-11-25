package org.main;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CanvasPanel2 extends JPanel {
    public static CanvasPanel2 instance = new CanvasPanel2();

    private MigLayout layout;

    private boolean displayGraphs = false;

    private List<GLJPanel> primaryGraphs;
    private List<GLJPanel> secondaryGraphs;

    private Dimension canvasDimension = new Dimension(2, 1);

    private boolean runningSetup = false;

    public CanvasPanel2() {
        StringBuilder columnConstraints = new StringBuilder(Theme.graphPadding + "[][][][][]" + Theme.graphPadding);
        StringBuilder rowConstraints = new StringBuilder(Integer.toString(Theme.graphPadding));
        for (int i = 0; i < canvasDimension.getWidth() - 1; i++) {
            columnConstraints.append("[]").append(Theme.graphPadding);
        }
        for (int i = 0; i < canvasDimension.getHeight(); i++) {
            rowConstraints.append("[]").append(Theme.graphPadding);
        }

        layout = new MigLayout("fill", columnConstraints.toString(), rowConstraints.toString());

        primaryGraphs = new ArrayList<>(Collections.nCopies((int) canvasDimension.getHeight(), null));
        secondaryGraphs = new ArrayList<>(Collections.nCopies((int) ((canvasDimension.getWidth() - 1) * canvasDimension.getHeight()), null));

        setLayout(layout);
        setBackground(Theme.canvasBackground);

        initializeGraphs();
        setupCanvasLayout();
    }

    private void setupCanvasLayout() {

    }

    private void initializeGraphs() {
        for (int column = 0; column < canvasDimension.getWidth(); column++) {
            List<Graph> existingGraphs = CanvasController.getGraphs();
            for (int row = 0; row < canvasDimension.getHeight(); row++) {

            }
        }
    }


    public void updateCanvasDimension(char x, int n) {
        if (x == 'w')
            canvasDimension = new Dimension(n, (int) canvasDimension.getHeight());
         else if (x == 'h')
            canvasDimension = new Dimension((int) canvasDimension.getWidth(), n);
    }
}
