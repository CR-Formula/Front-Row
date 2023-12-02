package org.main;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.awt.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class OpenGLTimeDomain extends PrimaryGraph implements OpenGLModel {
    private double maxYValue = 1;
    private double minYValue = -1;

    public OpenGLTimeDomain(int graphX, int graphY, int graphWidth, int graphHeight) {
        super(graphX, graphY, graphWidth, graphHeight);
        this.datasets = new ArrayList<Dataset>();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        // Utilize DoubleBuffer instead of drawing each vertex

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

                maxYValue = Math.max(maxYValue, dataset.getSample(dataset.getLength() - drawSampleCount));
                minYValue = Math.min(minYValue, dataset.getSample(dataset.getLength() - drawSampleCount));
                double range = maxYValue - minYValue;

                gl.glBegin(GL2.GL_LINE_STRIP);
                double initialX = -1.0;
                double initialY = dataset.getSample(dataset.getLength() - drawSampleCount);
                initialY = (((initialY - minYValue) / range) * 2) + -1;
                gl.glVertex2d(initialX, initialY);

                for (int i = 1; i < drawSampleCount - 1; i++) {
                    if (autoDetectMaxMin) {
                        maxYValue = Math.max(maxYValue, dataset.getSample((dataset.getLength()) - (drawSampleCount - (i - 1))));
                        minYValue = Math.min(minYValue, dataset.getSample((dataset.getLength()) - (drawSampleCount - (i - 1))));
                    }
                    else {
                        maxYValue = dataset.getMax();
                        minYValue = dataset.getMin();
                    }
                    range = maxYValue - minYValue;

                    double sampleX = -1.0 + ((i) * diff);
                    double sampleY = dataset.getSample((dataset.getLength()) - (drawSampleCount - (i)));
                    sampleY = (((sampleY - minYValue) / range) * 2) + -1;

                    Color c = dataset.getColor();
                    gl.glColor3d(c.getRed() / 255.0, c.getGreen() / 255.0, c.getBlue() / 255.0);

                    gl.glVertex2d(sampleX, sampleY);
                }

                gl.glEnd();
            }
            
            if (mouseOnCanvas) {
                gl.glBegin(GL2.GL_LINES);
                gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

                Color mouseColor = new Color(255, 255, 255);
                gl.glColor3d(mouseColor.getRed() / 255.0, mouseColor.getBlue() / 255.0, mouseColor.getGreen() / 255.0);

                double mouseXReal = 0.0;
                Point mousePos = Frame.getWindows()[0].getMousePosition();
                if (mousePos != null) {
                    // -6 if windows, need more testing though
                    // will need to determine OS then make difference fix
                    mouseXReal = convertPointOverWidth(mousePos.getX()); 
                }

                gl.glVertex2d(mouseXReal, 1);
                gl.glVertex2d(mouseXReal, -1);

                gl.glEnd();
            }

        } catch (ConcurrentModificationException e) {
            System.out.println("Cannot draw dataset");
        }
    }

    public String toString() {
        return "Time Domain";
    }
}