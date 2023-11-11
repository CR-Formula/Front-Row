package org.main;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.text.Format;
import java.text.NumberFormat;

public class DatasetPanel extends JPanel {
    public static DatasetPanel instance = new DatasetPanel();

    private GroupLayout layout;
    public DatasetPanel() {
        layout = new GroupLayout(this);

        layout.setAutoCreateGaps(false);
        layout.setAutoCreateContainerGaps(false);

        setLayout(layout);
        setBackground(Theme.canvasBackground);

        String name = "RPM";
        String label = "Hz";
        int max = 15500;
        int min = 0;

        JTextField nameField = new JTextField(name);
        nameField.addActionListener(event -> {
            System.out.println(nameField.getText());
        });

        JTextField labelField = new JTextField(label);
        nameField.addActionListener(event -> {
            System.out.println(labelField.getText());
        });


        SpinnerNumberModel numberModel = new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        JSpinner maxField = new JSpinner(numberModel);
        maxField.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println(maxField.getValue());
            }
        });

        JSpinner minField = new JSpinner(numberModel);
        minField.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println(minField.getValue());
            }
        });

        add(nameField);
        add(labelField);
        add(maxField);
        add(minField);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(nameField)
                                .addComponent(labelField)
                                .addComponent(maxField)
                                .addComponent(minField)
                        )
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameField)
                        .addComponent(labelField)
                        .addComponent(maxField)
                        .addComponent(minField)
        );
    }



}
