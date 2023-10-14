package org.example;

import org.main.SecondaryGraph;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class buttonPanel extends JPanel {

    static buttonPanel instance = new buttonPanel();
    private GroupLayout layout;
    private JButton buttonA;
    private JButton buttonB;

    public buttonPanel(int graphX, int graphY, int graphWidth, int graphHeight) {
        super(graphX, graphY, graphWidth, graphHeight);

        layout = new GroupLayout(this);

        setLayout(layout);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        layout.setAutoCreateGaps(false);
        layout.setAutoCreateContainerGaps(false);


        buttonA = new JButton("Button A");
        buttonA.addActionListener(event -> {
            System.out.println(buttonA.getText());
        });

        buttonB = new JButton("Button B");
        buttonB.addActionListener(event -> {
            System.out.println(buttonB.getText());
        });

        add(buttonA);
        add(buttonB);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(buttonA)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonB)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonA)
                                .addComponent(buttonB)
                        )
        );
    }
}