package org.main;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.Animator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CanvasController {
    private static List<PrimaryGraph> primaryGraphs = new ArrayList<>();
    private static List<List<SecondaryGraph>> secondaryGraphs = new ArrayList<>();
    private static List<Graph> graphs = new ArrayList<>();

    final static GLProfile profile = GLProfile.get(GLProfile.GL2);
    final static GLCapabilities capabilities = new GLCapabilities(profile);

    public final static String TIME_DOMAIN = "TIME DOMAIN";
    public final static String DIAL = "DIAL";

    public static void addGraph(String type, PrimaryGraph graph, int index) {
        switch(type) {
            case (TIME_DOMAIN):
                primaryGraphs.add(new OpenGLTimeDomain(graph.graphX, graph.graphY, graph.graphWidth, graph.graphHeight));
                break;
            case (DIAL):
                secondaryGraphs.get(0).add(null);
        }
    }

    public static GLJPanel refreshGLJPanel(GLJPanel panel) {
        GLJPanel replacement = new GLJPanel(capabilities);

//        ((Graph) panel.getGLEventListener(0)).setPosition(container.getX(), container.getY(), container.getSize());
//
//        replacement.addGLEventListener(panel.getGLEventListener(0));
//        replacement.addMouseListener(panel.getMouseListeners()[0]);
//
//        replacement.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
//
//        Animator animator = new Animator(replacement);
//        animator.setUpdateFPSFrames(1, null);
//        animator.start();

        return replacement;
    }

    public static List<Graph> getGraphs() {
        return graphs;
    }
}
