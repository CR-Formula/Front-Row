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
    private int max;
    private int min;

    public Dataset(String name) {
        this.name = name;
        this.values = new ArrayList<Float>();
    }
    public Dataset(String name, int index, Color color) {
        this.name = name;
        this.values = new ArrayList<Float>();
        this.color = color;
        this.label = "";
    }

    public void add(float value) {
        values.add(value);
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

    public Color getColor() {
        return color;
    }

    public Float getSample(int i) {
        return values.get(i);
    }

    public Float getLastSample() {
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
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
