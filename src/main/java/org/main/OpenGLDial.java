package org.main;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.awt.*;
import java.util.ConcurrentModificationException;

public class OpenGLDial extends SecondaryGraph implements OpenGLModel {
    private float maxValue;
    private float minValue;

    public OpenGLDial(int graphX, int graphY, int graphWidth, int graphHeight) {
        super(graphX, graphY, graphWidth, graphHeight);
        this.maxValue = 1;
        this.minValue = -1;
    }

    public OpenGLDial(int graphX, int graphY, int graphWidth, int graphHeight, float maxValue, float minValue) {
        super(graphX, graphY, graphWidth, graphHeight);
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    public void setMaxMin(float maxValue, float minValue) {
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        if (dataset == null) return;

        final GL2 gl = drawable.getGL().getGL2();

//        Color backgroundColor = new Color(150, 150, 150, 255);
//        gl.glClearColor(backgroundColor.getRed() / 255.0f, backgroundColor.getBlue() / 255.0f, backgroundColor.getGreen() / 255.0f, backgroundColor.getAlpha() / 255.0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        double radius = Math.min(graphHeight, graphWidth) * 0.45;
        final double range = 2;

        float value = dataset.hasValues() ? dataset.getLastSample() : minValue;
        if (autoDetectMaxMin && value > maxValue) maxValue = value;
        else if (autoDetectMaxMin && value < minValue) minValue = value;
        else if (!autoDetectMaxMin) {
            maxValue = dataset.getMax();
            minValue = dataset.getMin();
        }

        final int minSampleCount = (int) (((value - minValue) / (maxValue - minValue)) * sampleCount);
        final int drawSampleCount = Math.min(minSampleCount, sampleCount);

        try {
            gl.glBegin(GL.GL_TRIANGLE_FAN);
            Color c1 = dataset.getColor();
            gl.glColor3d(c1.getRed() / 255.0, c1.getBlue() / 255.0, c1.getGreen() / 255.0);

            double originX = convertPointOverWidth((graphWidth / 2));
            double originY = convertPointOverHeight((graphHeight / 2) - (radius / 2));

            gl.glVertex2d(originX, originY);

            double increment = Math.PI / sampleCount;
            for (double angle = 0; angle < (Math.PI * ((double) drawSampleCount / sampleCount)); angle += increment) {
                gl.glVertex2d(-1 * (originX + ((float)Math.cos(angle) * convertValueOverWidth(radius))),originY + ((float)Math.sin(angle) * convertValueOverHeight(radius)));
            }
            gl.glEnd();

            gl.glBegin(GL.GL_TRIANGLE_FAN);

            Color c2 = new Color(0, 0, 0);
            gl.glColor3d(c2.getRed() / 255.0, c2.getBlue() / 255.0, c2.getGreen() / 255.0);

            gl.glVertex2d(originX, originY);

            radius = radius * 0.75;

            for (double angle = 0; angle < (Math.PI * ((double) drawSampleCount / sampleCount)); angle += increment) {
                gl.glVertex2d(-1 * (originX + ((float)Math.cos(angle) * convertValueOverWidth(radius))),originY + ((float)Math.sin(angle) * convertValueOverHeight(radius)));
            }
            gl.glEnd();
        } catch (ConcurrentModificationException e) {
            System.out.println("Cannot draw dataset");
        }
    }

    public String toString() {
        return "Dial";
    }
}
