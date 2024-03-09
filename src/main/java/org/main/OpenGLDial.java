package org.main;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class OpenGLDial extends SecondaryGraph implements GLEventListener {
    private float[] vertices;
    private float maxValue;
    private float minValue;
    private final int sampleCount = 250;
    private float[] screenMatrix = new float[16];

    public OpenGLDial() {
        this.maxValue = 1;
        this.minValue = -1;
    }

    public void setMaxMin(float maxValue, float minValue) {
        this.maxValue = maxValue;
        this.minValue = minValue;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2ES3 gl = glAutoDrawable.getGL().getGL2ES3();
//        super.setupOpenGL(glAutoDrawable);
        OpenGL.makeAllPrograms(gl);
        OpenGL.makeOrthoMatrix(screenMatrix, -1, 1, -1, 1, -10000, 10000);
        OpenGL.useMatrix(gl, screenMatrix);
        setVertices(glAutoDrawable);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        if (dataset == null) return;

        try {
            GL2ES3 gl = glAutoDrawable.getGL().getGL2ES3();

            float[] backgroundColor = new float[]{Theme.graphBackground.getRed() / 255.0f, Theme.graphBackground.getGreen() / 255.0f, Theme.graphBackground.getBlue() / 255.0f, 1.0f};
            OpenGL.drawBox(gl, backgroundColor, -1, -1, 1 - -1, 1 - -1);

            float value = dataset.hasValues() ? dataset.getLastSample() : minValue;
            if (autoDetectMaxMin && value > maxValue) maxValue = value;
            else if (autoDetectMaxMin && value < minValue) minValue = value;
            else if (!autoDetectMaxMin) {
                maxValue = dataset.getMax();
                minValue = dataset.getMin();
            }

            float percent = Math.max(Math.min(1f, (value - minValue) / (maxValue - minValue)), 0);
            float[] datasetColor = new float[]{1f, 0f, 0f, 1f};
            OpenGL.buffer.rewind();
            OpenGL.buffer.put(vertices);
            OpenGL.drawTrianglesXY(gl, GL3.GL_TRIANGLE_STRIP, datasetColor, OpenGL.buffer, (int)(percent * sampleCount));
            OpenGL.buffer.rewind();
//            System.out.println("Secondary");
//            printFps(glAutoDrawable);
        } catch (Exception e) {
            System.out.println("Cannot draw dataset");
        }
    }

    private void setVertices(GLAutoDrawable glAutoDrawable) {
        // Set current dimensions
        vertices = new float[(int)(sampleCount * 2)];
        graphWidth = glAutoDrawable.getSurfaceWidth();
        graphHeight = glAutoDrawable.getSurfaceHeight();

        final float desiredRadius = 0.7f;
        final float[] radiusOuter = {
                (graphWidth < graphHeight) ? desiredRadius : graphHeight / graphWidth * desiredRadius,
                graphHeight < graphWidth ? desiredRadius : graphWidth / graphHeight * desiredRadius
        };

        final float[] radiusInner = {radiusOuter[0] - (0.25f * radiusOuter[0]),
                radiusOuter[1] - (0.25f * radiusOuter[1])};

        float originX = 0;
        float originY = radiusOuter[1] / -2f;
        float increment = ((float) Math.PI / ((float) sampleCount / 2));
        int i = 0;
        for (double angle = 0; angle < Math.PI; angle += increment) {
            vertices[i]     = (-1 * (originX + ((float) Math.cos(angle) * radiusInner[0])));
            vertices[i + 1] = (originY + ((float)Math.sin(angle) * radiusInner[1]));
            vertices[i + 2] = (-1 * (originX + ((float) Math.cos(angle) * radiusOuter[0])));
            vertices[i + 3] = (originY + ((float)Math.sin(angle) * radiusOuter[1]));
            i += 4;
        }
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    public String toString() {
        return "Dial";
    }
}
