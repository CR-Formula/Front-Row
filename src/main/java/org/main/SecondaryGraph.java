package org.main;

public abstract class SecondaryGraph extends Graph{
    protected Dataset dataset;

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }
}
