package org.main;

import org.main.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ToolBarPanel extends JPanel {

    public static ToolBarPanel instance = new ToolBarPanel();
    private GroupLayout layout;
    private JButton buttonA;
    private JButton buttonB;
    private JComboBox listA;

    private ToolBarPanel() {
//        super();

        layout = new GroupLayout(this);

        setLayout(layout);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Theme.pageBackground);

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

        listA = new JComboBox<>(new String[]{"ListONe", "ListTwo"});
        listA.setBounds(10, 10, 50, 10);
        listA.addActionListener(event -> {
            System.out.println(listA.getSelectedItem());
        });


        add(listA);



        add(buttonA);
        add(buttonB);
//        add(box);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(buttonA)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonB)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(listA)

        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(buttonA)
                                .addComponent(buttonB)
                                .addComponent(listA)
                        )
        );
    }
}