package org.main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ToolbarPanel extends JPanel {

    public static ToolbarPanel instance = new ToolbarPanel();
    private GroupLayout layout;
    private JButton connectButton;
    private JComboBox<String> inputTypeOptions;

    private ToolbarPanel() {
        super();

        layout = new GroupLayout(this);

        setLayout(layout);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Theme.pageBackground);

        layout.setAutoCreateGaps(false);
        layout.setAutoCreateContainerGaps(false);

        inputTypeOptions = new JComboBox<>(new String[] {"String A", "String B", "String C"});
        Dimension comboBoxAMaxSize = new Dimension((int) (inputTypeOptions.getPreferredSize().getWidth()  * 1.1), (int) inputTypeOptions.getPreferredSize().getHeight());
        inputTypeOptions.setMaximumSize(comboBoxAMaxSize);
        inputTypeOptions.addActionListener(event -> {
            System.out.println(inputTypeOptions.getSelectedItem());
        });

        connectButton = new JButton("Button B");
        connectButton.addActionListener(event -> {
            System.out.println(connectButton.getText());
        });

        add(inputTypeOptions);
        add(connectButton);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED,
                                GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(inputTypeOptions)
                                .addGap(Theme.toolbarPadding)
                                .addComponent(connectButton)
                        )
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(inputTypeOptions)
                        .addComponent(connectButton)
        );
    }
}