package org.main;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

public class OpenGLTimeDomainInfoLayer extends JPanel {
    private List<Dataset> datasetList;

    private MigLayout layout;

    public OpenGLTimeDomainInfoLayer() {
        setupLayout();

    }

    private void setupLayout() {
        String columnConstraints = generateConstraints(5, 10);
        String rowConstraints = generateConstraints(2, 20);


        layout = new MigLayout("fill", columnConstraints, rowConstraints);
        setLayout(layout);
    }

    private String generateConstraints(int padding, double dimension) {
        StringBuilder constraints = new StringBuilder(padding);
        for (int i = 0; i < dimension; i++) {
            constraints.append("[]").append(padding);
        }
        return constraints.toString();
    }
}
