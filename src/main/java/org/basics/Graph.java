package org.basics;

import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Graph extends MouseAdapter implements GLEventListener {
    protected int sampleCount = 10000;
    protected List<Dataset> datasets;

    protected double graphX;
    protected double graphY;
    protected double graphWidth;
    protected  double graphHeight;

    protected boolean mouseOnCanvas;

    public Graph(int graphX, int graphY, int graphWidth, int graphHeight) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        System.out.println(e);
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

    private void setSampleCount(int sampleCount) {
        this.sampleCount = sampleCount;
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {

    }
}
