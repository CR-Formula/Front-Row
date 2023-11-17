package org.main;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Graph extends MouseAdapter implements GLEventListener {
    protected static Point frameLocation;

    protected int sampleCount = 10000;
    protected List<Dataset> datasets;

    protected int graphX;
    protected int graphY;
    protected int graphWidth;
    protected int graphHeight;

    protected static boolean mouseOnCanvas;

    protected boolean autoDetectMaxMin = false;

    public Graph(int graphX, int graphY, int graphWidth, int graphHeight) {
        this.graphX = graphX;
        this.graphY = graphY;
        this.graphWidth = graphWidth;
        this.graphHeight = graphHeight;
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

    protected double convertPointOverHeight(double value) {
        return ((value / graphHeight) * (2)) + -1;
    }

    protected double convertPointOverWidth(double value) {
        return ((value / graphWidth) * (2)) + -1;
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
}
