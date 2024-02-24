package org.main;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.swing.*;
import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class OpenGLTimeDomain extends PrimaryGraph implements OpenGLModel {
    private int[] vboIds;
    private double maxYValue = 1;
    private double minYValue = -1;

    private static double leftMargin = 0.1;
    private static double bottomMargin = 0.25;
    private static double topMargin = 0.15;

    private GL2 gl;
//    private GLUT glut;

    public OpenGLTimeDomain(int graphX, int graphY, int graphWidth, int graphHeight) {
        super(graphX, graphY, graphWidth, graphHeight);
        this.datasets = new ArrayList<Dataset>();
    }

    public void init(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL2();

        vboIds = new int[1];
        gl.glGenBuffers(1, vboIds, 0);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        if (datasets == null) return;

        glut = new GLUT();

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
        gl.glClear(GL2.GL_BUFFER);

        final int minSampleCount = DatasetController.getLastSampleIndex();
        final double diff = (2.0 - leftMargin) / ((double) sampleCount);

        double graphXCo;
        double graphYCo;
        double labelXCo = -1 + leftMargin + 0.1;
        double labelYCo = 1 - topMargin;

        drawTickMarks();

        try {
            for (Dataset dataset : datasets) {
                OpenGL.drawLargeText(gl, "Test", 0, 0, 0f);
                dataset.setRecentMax(Integer.MIN_VALUE);
                dataset.setRecentMin(Integer.MAX_VALUE);
                final int drawSampleCount = Math.min(minSampleCount, sampleCount);
                if (drawSampleCount < 2)
                    return;

//                autoDetectMaxMin = dataset.autoDetectMaxMin;
                maxYValue = Math.max(maxYValue, dataset.getSample(dataset.getLength() - drawSampleCount));
                minYValue = Math.min(minYValue, dataset.getSample(dataset.getLength() - drawSampleCount));
                double range = maxYValue - minYValue;

                Color c = dataset.getColor();
                gl.glColor3d(c.getRed() / 255.0, c.getGreen() / 255.0, c.getBlue() / 255.0);

                double initialX = -1.0 + leftMargin;
                double initialY = dataset.getSample(dataset.getLength() - drawSampleCount);
                initialY = (((initialY - minYValue) / range) * (2 - bottomMargin)) + -1 + bottomMargin;

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
                    float sample = dataset.getSample((dataset.getLength()) - (drawSampleCount - (i)));
                    if (sample > dataset.getRecentMax())
                        dataset.setRecentMax(sample);
                    if (sample < dataset.getRecentMax())
                        dataset.setRecentMin(sample);

                    range = maxYValue - minYValue;

                    double sampleX = -1 + leftMargin + ((i) * diff);
                    double sampleY = dataset.getSample((dataset.getLength()) - (drawSampleCount - (i)));
                    sampleY = (((sampleY - minYValue) / range) * (2 - bottomMargin)) + -1 + bottomMargin;

                    verticesList.add((float) sampleX);
                    verticesList.add((float) sampleY);
                }
                float[] datasetVertices = new float[verticesList.size()];
                for (int i = 0; i < verticesList.size(); i++)
                    datasetVertices[i] = verticesList.get(i);
                drawBuffers(datasetVertices, gl.GL_LINE_STRIP);


                gl.glColor3d(1.0f, 1.0f, 1.0f);

                gl.glRasterPos2d(labelXCo, labelYCo);
                String label = dataset.getLabel().isEmpty() ? dataset.getName() : dataset.getLabel();
//                glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, label);

                graphXCo = labelXCo + label.length() / 40.0;
                graphYCo = labelYCo;
                gl.glRasterPos2d(graphXCo, graphYCo);
                String currentValue = String.format("%.2f", dataset.getLastSample());
//                glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, currentValue);

                labelYCo -= 0.15;

            }

            float[] marginVertices = new float[]{
                    -1, -1,
                    1, -1,
                    1, (float) (-1 + bottomMargin - Theme.graphMinPadding),
                    -1, (float) (-1 + bottomMargin - Theme.graphMinPadding),

                    -1, 1,
                    (float) leftMargin - 1, 1,
                    (float) leftMargin - 1, -1,
                    -1, -1
            };

            gl.glColor3d(1.0f, 1.0f, 1.0f);
            gl.glRasterPos2d(.6, 1 - topMargin);
            String maxValue = "Maximum: " + String.format("%.2f", maxYValue);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, maxValue);
            drawBuffers(marginVertices, GL2.GL_LINE_STRIP);
            
        } catch (ConcurrentModificationException e) {
            System.out.println("Cannot draw dataset");
        }
    }

    private void drawTickMarks() {
        int numXTicks = 5;
        double xTickOffset = 0.1;
        double xTickLength = 0.05;

        int numYTicks = 5;
        double yTickLength = 0.025;

        double xTickInterval = (2 - leftMargin) / (double) numXTicks;
        double yTickInterval = (2 - bottomMargin) / ((double) numYTicks);

        int yMidPoint = numYTicks / 2 + 1;
        double yTickOffset = Math.abs((1 - bottomMargin / 2) - (yMidPoint * yTickInterval));

        double graphXMin = leftMargin - 1;
        double graphYMin = bottomMargin - 1;
        double graphXMax = 1;
        double graphYMax = 1;

        float[] gridLinesVertices = new float[(numXTicks + numYTicks) * 4];
        int gridIndex = 0;

        float[] xTickVertices = new float[numXTicks * 4];
        for (int i = 0; i < numXTicks; i++) {
            double x = (leftMargin - 1) + i * xTickInterval + xTickOffset;
            int index = i * 4;
            xTickVertices[index] = (float) x;
            xTickVertices[index + 1] = (float) (graphYMin - Theme.graphMinPadding);
            xTickVertices[index + 2] = (float) x;
            xTickVertices[index + 3] = (float) ((graphYMin - Theme.graphMinPadding) - (xTickLength / (i % 2 + 1)));

            gridLinesVertices[gridIndex++] = (float) x;
            gridLinesVertices[gridIndex++] = (float) graphYMax;
            gridLinesVertices[gridIndex++] = (float) x;
            gridLinesVertices[gridIndex++] = (float) (graphYMin - Theme.graphMinPadding);
        }

        float[] yTickVertices = new float[numYTicks * 4];
        for (int i = 0; i < numYTicks; i++) {
            double y = graphYMin + i * yTickInterval + yTickOffset;
            int index = i * 4;
            yTickVertices[index] = (float) graphXMin;
            yTickVertices[index + 1] = (float) y;
            yTickVertices[index + 2] = (float) (graphXMin - (yTickLength / (i % 2 + 1)));
            yTickVertices[index + 3] = (float) y;

            gridLinesVertices[gridIndex++] = (float) graphXMin;
            gridLinesVertices[gridIndex++] = (float) y;
            gridLinesVertices[gridIndex++] = (float) graphXMax;
            gridLinesVertices[gridIndex++] = (float) y;

            // Center label at some point
            gl.glRasterPos2d(-1 + leftMargin / 3, y);
            double rawValue = (y - graphYMin) / (graphYMax - graphYMin) * (maxYValue - minYValue) + minYValue;
            rawValue = (Math.abs(rawValue) < 0.001) ? 0 : rawValue;

            String rawLabel = String.format("%.2f", rawValue);
//            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_12, rawLabel);
        }


        gl.glColor4f(255, 255, 255, 1.0f);
        drawBuffers(xTickVertices, gl.GL_LINES);
        drawBuffers(yTickVertices, gl.GL_LINES);

        gl.glColor4f(255, 255, 255, 0.1f);
        drawBuffers(gridLinesVertices, gl.GL_LINES);
    }

    // Binding to multiple buffer objects and rendering their vertices simultaneously may improve performance
    // Sets of vertices are currently rendered one at a time, which cleans up vboId assignments
    private void drawBuffers(float[] vertices, int primitivePolygon){
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboIds[0]);
        FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(vertices);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, (long) vertexBuffer.limit() * Buffers.SIZEOF_FLOAT, vertexBuffer, GL2.GL_STATIC_DRAW);
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glVertexPointer(2, GL2.GL_FLOAT, 0, 0);
        gl.glDrawArrays(primitivePolygon, 0, vertices.length/2);

        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
        gl.glDisable(GL2.GL_BLEND);
    }

    public static void setLeftMargin(double leftMargin) {
       OpenGLTimeDomain.leftMargin = leftMargin;
    }

    public static void setBottomMargin(double bottomMargin) {
        OpenGLTimeDomain.bottomMargin = bottomMargin;
    }

    public static double getLeftMargin() {
        return leftMargin;
    }

    public static double getBottomMargin() {
        return bottomMargin;
    }

    public String toString() {
        return "Time Domain";
    }
}