package org.main;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class DatasetPanel extends JPanel {
    public static DatasetPanel instance = new DatasetPanel();

    private List<DatasetRowPanel> datasetRowPanels = new ArrayList<>();

    private GroupLayout layout;
    public DatasetPanel() {
        layout = new GroupLayout(this);

        layout.setAutoCreateGaps(false);
        layout.setAutoCreateContainerGaps(false);

        setLayout(layout);
        setBackground(Theme.canvasBackground);

        for (int i = 0; i < DatasetController.getDatasets().size(); i++) {
            datasetRowPanels.add(new DatasetRowPanel(i, DatasetController.getDataset(i)));
        }

        GroupLayout.ParallelGroup horizontalGroup = layout.createParallelGroup();
        for (int i = 0; i < DatasetController.getDatasets().size(); i++) {
            horizontalGroup.addComponent(datasetRowPanels.get(i));
        }

        GroupLayout.SequentialGroup verticalGroup = layout.createSequentialGroup();
        verticalGroup.addGap(Theme.datasetRowPadding);
        for (int i = 0; i < DatasetController.getDatasets().size(); i++) {
            verticalGroup.addComponent(datasetRowPanels.get(i));
        }
        verticalGroup.addGap(Theme.datasetRowPadding);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED,
                                GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
                        .addGroup(horizontalGroup)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED,
                                GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
        );

        layout.setVerticalGroup(verticalGroup);
    }



}
