package org.main;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

public class PrimaryGraph extends Graph {
    private PrimaryGraphInfoLayer dataLayer;

    public PrimaryGraph(int graphX, int graphY, int graphWidth, int graphHeight) {
        super(graphX, graphY, graphWidth, graphHeight);
    }

    public PrimaryGraphInfoLayer getDataLayer() { return dataLayer; }

    public void setDataLayer(PrimaryGraphInfoLayer dataLayer) { this.dataLayer = dataLayer; }
}
