package org.main;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class OpenGLTimeDomain extends PrimaryGraph implements OpenGLModel {
    private double maxYValue = 1;
    private double minYValue = -1;
    private int vboId;

    public OpenGLTimeDomain(int graphX, int graphY, int graphWidth, int graphHeight) {
        super(graphX, graphY, graphWidth, graphHeight);
        this.datasets = new ArrayList<Dataset>();
    }
    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL2 gl = glAutoDrawable.getGL().getGL2();


        int[] vboIds = new int[1];
        // Generate a buffer object name/ID
        gl.glGenBuffers(1, vboIds, 0);
        vboId = vboIds[0];
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        // Utilize DoubleBuffer instead of drawing each vertex

        if (datasets == null) return;

        final GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glClear(GL2.GL_BUFFER);

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

                double initialX = -1.0;
                double initialY = dataset.getSample(dataset.getLength() - drawSampleCount);
                initialY = (((initialY - minYValue) / range) * 2) + -1;

                ArrayList<Float> verticesList = new ArrayList<>();
                verticesList.add((float) initialX);
                verticesList.add((float) initialY);

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

                    verticesList.add((float) sampleX);
                    verticesList.add((float) sampleY);
                }

                float[] vertices = new float[verticesList.size()];
                for (int i = 0; i < verticesList.size(); i++)
                    vertices[i] = verticesList.get(i);

                gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboId);
                FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(vertices);
                gl.glBufferData(GL2.GL_ARRAY_BUFFER, (long) vertexBuffer.limit() * Buffers.SIZEOF_FLOAT, vertexBuffer, GL2.GL_STATIC_DRAW);
                gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
                gl.glVertexPointer(2, GL2.GL_FLOAT, 0, 0);
                gl.glDrawArrays(GL2.GL_LINE_STRIP, 0, vertices.length/2);

                gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
                gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
            }
            
            if (mouseOnCanvas) {

                Color mouseColor = new Color(255, 255, 255);
                gl.glColor3d(mouseColor.getRed() / 255.0, mouseColor.getBlue() / 255.0, mouseColor.getGreen() / 255.0);

                double mouseXReal = 0.0;
                Point mousePos = Frame.getWindows()[0].getMousePosition();
                if (mousePos != null) {
                    // -6 if windows, need more testing though
                    // will need to determine OS then make difference fix
                    mouseXReal = convertPointOverWidth(mousePos.getX()); 
                }

                float[] mouseVertices = {
                        (float) mouseXReal, 1,
                        (float) mouseXReal, -1
                };

                gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboId);
                FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(mouseVertices);
                gl.glBufferData(GL2.GL_ARRAY_BUFFER, (long) vertexBuffer.limit() * Buffers.SIZEOF_FLOAT, vertexBuffer, GL2.GL_STATIC_DRAW);
                gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
                gl.glVertexPointer(2, GL2.GL_FLOAT, 0, 0);
                gl.glDrawArrays(GL2.GL_LINES, 0, 2);

                gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
                gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
            }

        } catch (ConcurrentModificationException e) {
            System.out.println("Cannot draw dataset");
        }
    }

    public String toString() {
        return "Time Domain";
    }
}