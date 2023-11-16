package org.main;

import javax.swing.*;

public class DatasetRowPanel extends JPanel {
    private int index;
    private Dataset dataset;

    public DatasetRowPanel(int index, Dataset dataset) {
        this.index = index;
        this.dataset = dataset;
    }

    public void updateDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public void updateDatasetName(String name) {
        this.dataset.setName(name);
    }

    public void updateDatasetLabel(String label) {
        this.dataset.setLabel(label);
    }

    public void updateDatasetMax(int max) {
        this.dataset.setMax(max);
    }

    public void updateDatasetMin(int min) {
        this.dataset.setMin(min);
    }
}
