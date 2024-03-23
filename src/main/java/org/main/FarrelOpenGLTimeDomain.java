package org.main;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;

import javax.swing.*;
import java.awt.*;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class FarrelOpenGLTimeDomain extends PrimaryGraph implements OpenGLModel {
    private double maxYValue = 1;
    private double minYValue = -1;
    private GL2ES3 gl;
    private GLUT glut;

    private int timePassed = 0;
    private int lastTime = 0;
    private double xTickOffset = -2.25;

    public FarrelOpenGLTimeDomain(int graphX, int graphY, int graphWidth, int graphHeight) {
        super(graphX, graphY, graphWidth, graphHeight);
        this.datasets = new ArrayList<Dataset>();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height){
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        gl = glAutoDrawable.getGL().getGL2ES3();

        gl.glEnable(GL3.GL_BLEND);
        gl.glBlendFunc(GL3.GL_SRC_ALPHA, GL3.GL_ONE_MINUS_SRC_ALPHA);
        gl.setSwapInterval(1);
        float scalingFactor = (int) Math.round((double) Toolkit.getDefaultToolkit().getScreenResolution() / 100.0);
        Theme.initialize(gl, scalingFactor);
        OpenGL.makeAllPrograms(gl);
        float[] screenMatrix = new float[16];
        OpenGL.makeOrthoMatrix(screenMatrix, -1, 1, -1, 1, -10000, 10000);
        OpenGL.useMatrix(gl, screenMatrix);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        if (datasets == null) return;

//        glut = new GLUT();

        final int minSampleCount = DatasetController.getLastSampleIndex();
        final double diff = (2.0 - leftMargin) / ((double) sampleCount);

        double graphXCo;
        double graphYCo;
        double labelXCo = -1 + leftMargin + 0.1;
        double labelYCo = 1 - topMargin;

        timePassed = datasets.get(0).getLength() - lastTime;
        lastTime = datasets.get(0).getLength();

        OpenGL.drawBox(gl, new float[]{0.0f,0.0f,0.0f,1f}, -1,-1, 2, 2);
        try {
            for (Dataset dataset : datasets) {
//                OpenGL.drawLargeText(gl, "Test", 0, 0, 0f);
                dataset.setRecentMax(Integer.MIN_VALUE);
                dataset.setRecentMin(Integer.MAX_VALUE);
                final int drawSampleCount = Math.min(minSampleCount, sampleCount);
                if (drawSampleCount < 2)
                    return;

//                autoDetectMaxMin = dataset.autoDetectMaxMin;
                maxYValue = Math.max(maxYValue, dataset.getSample(dataset.getLength() - drawSampleCount));
                minYValue = Math.min(minYValue, dataset.getSample(dataset.getLength() - drawSampleCount));
                double range = maxYValue - minYValue;

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

                FloatBuffer vertexBuffer = Buffers.newDirectFloatBuffer(datasetVertices);
                Color c = dataset.getColor();
                OpenGL.drawLinesXy(gl, GL3.GL_LINE_STRIP, new float[]{c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f,1f}, vertexBuffer, datasetVertices.length / 2);
            }

            drawTickMarks();

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

            FloatBuffer marginBuffer = Buffers.newDirectFloatBuffer(marginVertices);
            OpenGL.drawLinesXy(gl, GL3.GL_LINE_STRIP, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, marginBuffer, marginVertices.length / 2);
//            System.out.println("Primary");
//            printFps(drawable);
        } catch (ConcurrentModificationException e) {
            System.out.println("Cannot draw dataset");
        }
    }

    private void drawTickMarks() {
        double graphXMin = leftMargin - 1;
        double graphYMin = bottomMargin - 1;
        double graphXMax = 1;
        double graphYMax = 1;

        int numXTicks = 5;
        double xTickIncrement = (double) timePassed * (graphXMax - graphXMin) / (sampleCount - 2);
        if(DatasetController.getLastSampleIndex() > sampleCount)
            xTickOffset -= xTickIncrement;

        double xTickLength = 0.05;

        int numYTicks = 5;
        double yTickLength = 0.025;

        double xTickInterval = (2 - leftMargin) / (double) numXTicks;
        double yTickInterval = (2 - bottomMargin) / ((double) numYTicks);

        int yMidPoint = numYTicks / 2 + 1;
        double yTickOffset = Math.abs((1 - bottomMargin / 2) - (yMidPoint * yTickInterval));


        float[] gridLinesVertices = new float[(numXTicks + numYTicks) * 4];
        int gridIndex = 0;

        float[] xTickVertices = new float[numXTicks * 4];
        for (int i = 0; i < numXTicks; i++) {
            double x = (leftMargin - 1) + i * xTickInterval + xTickOffset;
            x %= (graphXMax - graphXMin);
            x++;

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
        }

        FloatBuffer xTickBuffer = Buffers.newDirectFloatBuffer(xTickVertices);
        OpenGL.drawLinesXy(gl, GL3.GL_LINES, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, xTickBuffer, xTickVertices.length / 2);

        FloatBuffer yTickBuffer = Buffers.newDirectFloatBuffer(yTickVertices);
        OpenGL.drawLinesXy(gl, GL3.GL_LINES, new float[]{1.0f, 1.0f, 1.0f, 1.0f}, yTickBuffer, yTickVertices.length / 2);

        FloatBuffer gridLineBuffer = Buffers.newDirectFloatBuffer(gridLinesVertices);
        OpenGL.drawLinesXy(gl, GL3.GL_LINES, new float[]{1.0f, 1.0f, 1.0f, 0.2f}, gridLineBuffer, gridLinesVertices.length / 2);

    }

    public String toString() {
        return "Time Domain";
    }

    public static void main(String[] args) throws InterruptedException {
        final GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        final GLCanvas glCanvas = new GLCanvas(capabilities);
        final GLCanvas glCanvas2 = new GLCanvas(capabilities);

        DataInput.connect(DataInput.TEST);

        FarrelOpenGLTimeDomain td = new FarrelOpenGLTimeDomain(0,0,0,0);
        java.util.List<Dataset> l = new ArrayList<>();
        l.add(DatasetController.getDataset(0));
        td.setDatasets(l);
        td.toggleAutoDetectMaxMin();

        OpenGLTimeDomain td2 = new OpenGLTimeDomain(0,0,0,0);
        List<Dataset> l2 = new ArrayList<>();
        l2.add(DatasetController.getDataset(1));
        td2.setDatasets(l2);
        td2.toggleAutoDetectMaxMin();


        glCanvas.addGLEventListener(td);
        glCanvas.setSize(400, 400);

        glCanvas2.addGLEventListener(td2);
        glCanvas.setSize(400, 400);

        Animator animator = new Animator(glCanvas);
        animator.setUpdateFPSFrames(1, null);
        animator.start();

        Animator animator2 = new Animator(glCanvas2);
        animator2.setUpdateFPSFrames(1, null);
        animator2.start();

        Box screen = new Box(BoxLayout.Y_AXIS);
        screen.add(glCanvas);
        screen.add(glCanvas2);

        final JFrame frame = new JFrame ("Moving Buffer Data");

        frame.getContentPane().add(screen);
        frame.setSize(frame.getContentPane().getPreferredSize());
        frame.setVisible(true);

        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
    }
}