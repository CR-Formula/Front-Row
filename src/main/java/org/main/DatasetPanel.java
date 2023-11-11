package org.main;

import javax.swing.*;

public class DatasetPanel extends JPanel {
    public static DatasetPanel instance = new DatasetPanel();

    private GroupLayout layout;
    public DatasetPanel() {
        layout = new GroupLayout(this);

        layout.setAutoCreateGaps(false);
        layout.setAutoCreateContainerGaps(false);

        setLayout(layout);
        setBackground(Theme.canvasBackground);
    }

}
