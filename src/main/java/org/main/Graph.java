package org.main;

import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Graph extends MouseAdapter implements GLEventListener {
    protected static Point frameLocation;

    protected int sampleCount = 1000;
    protected List<Dataset> datasets = new ArrayList<Dataset>();

    protected int graphX;
    protected int graphY;
    protected float graphWidth;
    protected float graphHeight;

    protected float leftPlotX = -1;
    protected float rightPlotX = 1;
    protected float topPlotY = 1;
    protected float bottomPlotY = -1;

    protected static boolean mouseOnCanvas;

    protected boolean autoDetectMaxMin = false;

    private boolean firstTime = true;

    public Graph(int graphX, int graphY, int graphWidth, int graphHeight) {
        this.graphX = graphX;
        this.graphY = graphY;
        this.graphWidth = graphWidth;
        this.graphHeight = graphHeight;
    }
    public Graph() {
        this(0,0,0,0);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        System.out.println(e);
        mouseOnCanvas = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseOnCanvas = false;
    }

    public void setDatasets(List<Dataset> datasets) {
        this.datasets.addAll(datasets);
    }
    public List<Dataset> getDatasets(){
        return datasets;
    }

    public void setPosition(int x, int y, Dimension location) {
        this.graphX = x;
        this.graphY = y;
        this.graphWidth = location.width;
        this.graphHeight = location.height;
    }

    public void setSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }

    protected float convertPointOverHeight(double value) {
        return (float) (((value / graphHeight) * (2)) + -1);
    }

    protected float convertPointOverWidth(double value) {
        return (float) (((value / graphWidth) * (2)) + -1);
    }

    protected double convertValueOverHeight(double value) {
        return ((value / graphHeight) * (2));
    }

    protected double convertValueOverWidth(double value) {
        return ((value / graphWidth) * (2));
    }

    protected static void setFrameLocation(Point location) { frameLocation = location; }

    public void toggleAutoDetectMaxMin() {
        autoDetectMaxMin = !autoDetectMaxMin;
    }

    protected void setupOpenGL(GLAutoDrawable glAutoDrawable) {
        if (!firstTime) return;
        firstTime = false;
        GL2ES3 gl = glAutoDrawable.getGL().getGL2ES3();
        gl.glEnable(GL3.GL_BLEND);
        gl.glBlendFunc(GL3.GL_SRC_ALPHA, GL3.GL_ONE_MINUS_SRC_ALPHA);
        gl.setSwapInterval(1);
        float scalingFactor = (int) Math.round((double) Toolkit.getDefaultToolkit().getScreenResolution() / 100.0);
//        Theme.initialize(gl, scalingFactor);
        OpenGL.makeAllPrograms(gl);
        float[] screenMatrix = new float[16];
        OpenGL.makeOrthoMatrix(screenMatrix, -1, 1, -1, 1, -10000, 10000);
        OpenGL.useMatrix(gl, screenMatrix);
    }

    protected void printFps(GLAutoDrawable glAutoDrawable) {
        System.out.println(glAutoDrawable.getAnimator().getLastFPS());
    }
}
