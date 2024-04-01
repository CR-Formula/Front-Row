package org.main;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Dataset {
    private List<Float> values;
    private String name;
    private String label;
    private Color color;
    private int index;
    private float max;
    private float min;
    private float recentMax;
    private float recentMin;
    protected boolean autoDetectMaxMin = false;

    public Dataset(String name) {
        this.name = name;
        this.values = new ArrayList<Float>();
        this.max = 1;
        this.min = -1;
    }
    public Dataset(String name, int index, Color color) {
        this.name = name;
        this.values = new ArrayList<Float>();
        this.color = color;
        this.label = "";
        this.max = 1;
        this.min = -1;
    }

    public void add(float value) {
        values.add(value);
    }
    public String toString(){
        return getLabel().isEmpty() ? getName() : getLabel();
    }
    public List<Float> getValues() {
        return values;
    }

    public int getLength() {
        return values.size();
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public float getMax() {
        return max;
    }

    public float getMin() {
        return min;
    }

    public Color getColor() {
        return color;
    }

    public float getSample(int i) {
        return values.get(i);
    }

    public float getLastSample() {
        return values.size() > 0 ? values.get(values.size() - 1) : 0.0f;
    }

    public boolean hasValues() {
        return values.size() > 0;
    }

    public int getIndex() {
        return index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setColor(Color color) {
        this.color = color;
        System.out.println("Dataset: " + color.getRGB());
    }

    public void setMax(float max) {
        this.max = max;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public void setRecentMax(float max) {this.recentMax = max;}

    public void setRecentMin(float min) {this.recentMin = min;}

    public float getRecentMax() {return recentMax;}

    public float getRecentMin() {return recentMin;}
}
