package org.main;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class PrimaryGraph extends Graph {
    private PrimaryGraphInfoLayer dataLayer;
    protected static double leftMargin = 0.1;
    protected static double bottomMargin = 0.25;
    protected static double topMargin = 0.15;

    public PrimaryGraph(int graphX, int graphY, int graphWidth, int graphHeight) {
        super(graphX, graphY, graphWidth, graphHeight);
    }

    public PrimaryGraphInfoLayer getDataLayer() { return dataLayer; }

    public void setDataLayer(PrimaryGraphInfoLayer dataLayer) { this.dataLayer = dataLayer; }

    // Margin fields and methods likely temporary: just for testing purposes
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
}
