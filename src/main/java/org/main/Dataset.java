package org.main;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Dataset {
    private List<Float> values;
    private String name;
    private Color color;
    private int index;

    public Dataset(String name) {
        this.name = name;
        this.values = new ArrayList<Float>();
    }
    public Dataset(String name, int index, Color color) {
        this.name = name;
        this.values = new ArrayList<Float>();
        this.color = color;
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
}
