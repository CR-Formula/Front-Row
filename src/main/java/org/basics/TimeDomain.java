package org.basics;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class TimeDomain extends PrimaryGraph {
    private double maxYValue = Double.MIN_VALUE;
    private double minYValue = Double.MAX_VALUE;

    @Override
    public void display(GLAutoDrawable drawable) {
        if (datasets == null) return;

        final GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        final int minSampleCount = DatasetController.getLastSampleIndex();
        final double diff = 2.0 / ((double) sampleCount);

        try {
            for (Dataset dataset : datasets) {
                final int drawSampleCount = Math.min(minSampleCount, sampleCount);
                if (drawSampleCount < 2)
                    return;


                gl.glBegin(GL2.GL_LINE_STRIP);
                for (int i = 1; i < drawSampleCount - 1; i++) {
                    maxYValue = Math.max(maxYValue, dataset.getSample((dataset.getLength()) - (drawSampleCount - (i - 1))));
                    minYValue = Math.min(minYValue, dataset.getSample((dataset.getLength()) - (drawSampleCount - (i - 1))));
                    final double range = maxYValue - minYValue;

                    double sample1x = -1.0 + ((i - 1) * diff);
                    double sample2x = -1.0 + ((i) * diff);
                    double sample1y = dataset.getSample((dataset.getLength()) - (drawSampleCount - (i - 1)));
                    sample1y = (((sample1y - minYValue) / range) * 2) + -1;
                    double sample2y = dataset.getSample((dataset.getLength()) - (drawSampleCount - (i)));
                    sample2y = (((sample2y - minYValue) / range) * 2) + -1;

                    Color c = dataset.getColor();
                    gl.glColor3d(c.getRed() / 255.0, c.getBlue() / 255.0, c.getGreen() / 255.0);

                    gl.glVertex2d(sample1x, sample1y);
                    gl.glVertex2d(sample2x, sample2y);
                }

                gl.glEnd();
            }
            if (mouseOnCanvas) {
                gl.glBegin(GL2.GL_LINES);
                gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

                Color mouseColor = new Color(255, 255, 255);
                gl.glColor3d(mouseColor.getRed() / 255.0, mouseColor.getBlue() / 255.0, mouseColor.getGreen() / 255.0);

                double realMouseX = MouseInfo.getPointerInfo().getLocation().x;
                double mouseX = (((realMouseX - graphX) / (graphWidth)) * 2) + -1;
                System.out.println(graphX + " | " + graphWidth);
                System.out.println(realMouseX + " || " + mouseX);

                gl.glVertex2d(mouseX, 1);
                gl.glVertex2d(mouseX, -1);

                gl.glEnd();
            }

        } catch (ConcurrentModificationException e) {
            System.out.println("Cannot draw dataset");
        }
    }

    public TimeDomain(int graphX, int graphY, int graphWidth, int graphHeight) {
        super(graphX, graphY, graphWidth, graphHeight);
        this.datasets = new ArrayList<Dataset>();
    }

}