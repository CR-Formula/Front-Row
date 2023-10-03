package org.basics;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        final GLCanvas glCanvas1 = new GLCanvas(capabilities);
        final GLCanvas glCanvas2 = new GLCanvas(capabilities);
        final GLCanvas glCanvas3 = new GLCanvas(capabilities);
        final GLCanvas glCanvas4 = new GLCanvas(capabilities);

        TimeDomain td1 = new TimeDomain();
        TimeDomain td2 = new TimeDomain();
        TimeDomain td3 = new TimeDomain();
        TimeDomain td4 = new TimeDomain();

        final int graphWidth = 800;
        final int graphHeight = 200;

        glCanvas1.addGLEventListener(td1);
        glCanvas1.setSize(graphWidth, graphHeight);

        glCanvas2.addGLEventListener(td2);
        glCanvas2.setSize(graphWidth, graphHeight);

        glCanvas3.addGLEventListener(td3);
        glCanvas3.setSize(graphWidth, graphHeight);

        glCanvas4.addGLEventListener(td4);
        glCanvas4.setSize(graphWidth, graphHeight);

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
        graphs1.add(glCanvas2);

        Box graphs2 = new Box(BoxLayout.X_AXIS);
        graphs2.add(glCanvas3);
        graphs2.add(glCanvas4);

        Box screen = new Box(BoxLayout.Y_AXIS);
        screen.add(graphs1);
        screen.add(graphs2);

        frame.getContentPane().add(screen);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);

        DataInput.connect(DataInput.TEST);
        List<Dataset> l = new ArrayList<>();
        l.add(DatasetController.getDataset(0));
        td1.setDatasets(l);
        td2.setDatasets(l);
        td3.setDatasets(l);
        td4.setDatasets(l);
        l.add(DatasetController.getDataset(1));
        td2.setDatasets(l);
        td3.setDatasets(l);

        Thread.sleep(10000);

        l.add(DatasetController.getDataset(2));
        td1.setDatasets(l);
    }
}
