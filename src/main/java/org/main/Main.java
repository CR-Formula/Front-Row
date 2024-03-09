package org.main;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // TODO: Optimize the main and adding graphs
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        final int graphWidth = 600;
        final int graphHeight = 200;

        final GLCanvas glCanvas1 = new GLCanvas(capabilities);
        final GLCanvas glCanvas2 = new GLCanvas(capabilities);
        final GLCanvas glCanvas3 = new GLCanvas(capabilities);
        final GLCanvas glCanvas4 = new GLCanvas(capabilities);

        OpenGLTimeDomain td1 = new OpenGLTimeDomain(0, 0, graphWidth, graphHeight);
        OpenGLTimeDomain td2 = new OpenGLTimeDomain(0, graphHeight, graphWidth, graphHeight);
        OpenGLDial d1 = new OpenGLDial();
        OpenGLDial d2 = new OpenGLDial();

        glCanvas1.addGLEventListener(td1);
        glCanvas1.addMouseListener(td1);
        glCanvas1.setSize(graphWidth, graphHeight);

        glCanvas2.addGLEventListener(td2);
        glCanvas2.addMouseListener(td2);
        glCanvas2.setSize(graphWidth, graphHeight);

        glCanvas3.addGLEventListener(d1);
        glCanvas3.addMouseListener(d1);
        glCanvas3.setSize(graphHeight, graphHeight);

        glCanvas4.addGLEventListener(d2);
        glCanvas4.addMouseListener(d2);
        glCanvas4.setSize(graphHeight, graphHeight);

        Animator animator1 = new Animator(glCanvas1);
        animator1.setUpdateFPSFrames(1, null);
        animator1.start();

        Animator animator2 = new Animator(glCanvas2);
        animator2.setUpdateFPSFrames(1, null);
        animator2.start();

        Animator animator3 = new Animator(glCanvas3);
        animator3.setUpdateFPSFrames(1, null);
        animator3.start();

        Animator animator4 = new Animator(glCanvas4);
        animator4.setUpdateFPSFrames(1, null);
        animator4.start();

        final JFrame frame = new JFrame("Drawing");

        Box graphs1 = new Box(BoxLayout.X_AXIS);
        graphs1.add(glCanvas1);
        graphs1.add(glCanvas3);

        Box graphs2 = new Box(BoxLayout.X_AXIS);
        graphs2.add(glCanvas2);
        graphs2.add(glCanvas4);

        Box screen = new Box(BoxLayout.Y_AXIS);
        screen.add(graphs1);
        screen.add(graphs2);

        frame.getContentPane().add(screen);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);

        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

        frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent event) {
                Point location = frame.getLocationOnScreen();
                Graph.setFrameLocation(location);
            }

            public void componentResized(java.awt.event.ComponentEvent event) {
                int width=event.getComponent().getWidth();
                int height=event.getComponent().getHeight();
                glCanvas1.setSize((int) (width * 0.65),(int) (height * 0.45));
                ((Graph) glCanvas1.getGLEventListener(0)).setPosition(glCanvas1.getX(), glCanvas1.getY(), glCanvas1.getSize());
                glCanvas2.setSize((int) (width * 0.65),(int) (height * 0.45));
                ((Graph) glCanvas2.getGLEventListener(0)).setPosition(glCanvas2.getX(), glCanvas2.getY(), glCanvas2.getSize());
                glCanvas3.setSize((int) (width * 0.35),(int) (height * 0.45));
                ((Graph) glCanvas3.getGLEventListener(0)).setPosition(glCanvas1.getX() + glCanvas1.getWidth(), glCanvas3.getY(), glCanvas3.getSize());
                glCanvas4.setSize((int) (width * 0.35),(int) (height * 0.45));
                ((Graph) glCanvas4.getGLEventListener(0)).setPosition(glCanvas2.getX() + glCanvas2.getWidth(), glCanvas4.getY(), glCanvas4.getSize());
            }
        });

        DataInput.connect(DataInput.TEST);
        List<Dataset> l = new ArrayList<>();
        l.add(DatasetController.getDataset(0));
        td1.setDatasets(l);
        td2.setDatasets(l);
        d1.setDataset(l.get(0));
        l.add(DatasetController.getDataset(1));
        d2.setDataset(l.get(1));
        td2.setDatasets(l);

        Thread.sleep(10000);

        l.add(DatasetController.getDataset(2));
        td1.setDatasets(l);
    }
}
