package org.main;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;

public class PrimaryGraphInfoLayer extends JPanel {
    private List<Dataset> datasetList;

    private MigLayout layout;

    private int height = 25;
    private int width = 20;
    JLabel[][] labels = new JLabel[height][width];


    private DecimalFormat decimalFormat = new DecimalFormat("0.0");

    //⍃	⍄
    private final String AVERAGE = "⌻";
    private final String MAX = "⍓";
    private final String MIN = "⍌";

    public PrimaryGraphInfoLayer(List<Dataset> datasetList) {
        this.datasetList = datasetList;
        this.setOpaque(true);
        setupLayout();
        setupLabels();
        displayData();
    }

    public void displayData() {
        if (labels.length != height) return;
//        removeAll();

        for (int row = height - 1; row < height; row++) {
            for (int column = width - 3; column < width; column++) {
                labels[row][column].removeAll();
                labels[row][column].setText("⌻: " + column + " | " + row);
                labels[row][column].revalidate();
                labels[row][column].repaint();
                System.out.println("Value: " + labels[row][column].getText());
            }
        }
        revalidate();
    }

    private void setupLabels() {
        for (int row = height - 1; row < height; row++) {
            for (int column = 0; column < width; column++) {
                if (labels[row][column] == null) labels[row][column] = new JLabel();
                String location = "cell " + column + " " + row + ", grow";
                labels[row][column].setOpaque(true);
                labels[row][column].setFont(Theme.mediumFont);
                labels[row][column].setBackground(Color.BLACK);
                labels[row][column].setForeground(Color.WHITE);
                add(labels[row][column], location);
            }
        }
    }

    private void setupLayout() {
        String columnConstraints = generateConstraints(1, width);
        String rowConstraints = generateConstraints(1, height);

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
