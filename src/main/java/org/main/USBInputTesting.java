package org.main;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class USBInputTesting {

    public static void main(String[] args) throws InterruptedException {
        String[] openPorts = DataInput.getOpenUARTPorts();
        if (openPorts.length == 0) {
            System.out.println("No open ports.");
            return;
        }

        DataInput.setUARTPort(openPorts[0]);
        DataInput.connect(DataInput.UART);
        DatasetController.autoDetectDatasets(DataInput.UART);

        // Test each datasets value
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        final int graphWidth = 600;
        final int graphHeight = 200;

        final GLCanvas glCanvas1 = new GLCanvas(capabilities);

        TimeDomain td1 = new TimeDomain(0, 0, graphWidth, graphHeight);

        glCanvas1.addGLEventListener(td1);
        glCanvas1.addMouseListener(td1);
        glCanvas1.setSize(graphWidth, graphHeight);

        Animator animator1 = new Animator(glCanvas1);
        animator1.setUpdateFPSFrames(1, null);
        animator1.start();

        final JFrame frame = new JFrame("Drawing");

        Box graphs1 = new Box(BoxLayout.X_AXIS);
        graphs1.add(glCanvas1);

        Box screen = new Box(BoxLayout.Y_AXIS);
        screen.add(graphs1);

        frame.getContentPane().add(screen);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);
        td1.setSampleCount(1000);
        for (int i = 0; i < DatasetController.getDatasets().size(); i++) {
            List<Dataset> l = new ArrayList<>();
            l.add(DatasetController.getDataset(i));
            td1.setDatasets(l);
            System.out.println(l.get(0).getName() + "\t" + l.get(0).getColor().toString() + "\t" + l.get(0).getLastSample());
            Thread.sleep(400);
        }
    }
}
