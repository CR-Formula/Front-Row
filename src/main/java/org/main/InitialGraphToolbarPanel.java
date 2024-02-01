package org.main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class InitialGraphToolbarPanel extends JPanel {
    private GroupLayout layout;
    private JButton confirmButton;
    private JButton cancelButton;

    public InitialGraphToolbarPanel(InitialGraphPanel currentPanel){
        super();

        layout = new GroupLayout(this);

        setLayout(layout);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Theme.toolbarBackground);

        layout.setAutoCreateGaps(false);
        layout.setAutoCreateContainerGaps(false);

        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(event -> currentPanel.confirmChanges());

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> currentPanel.hideFrame());

        add(confirmButton);
        add(cancelButton);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED,
                                GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(cancelButton)
                                .addGap(Theme.toolbarPadding)
                                .addComponent(confirmButton)
                        )
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(confirmButton)
                        .addComponent(cancelButton)
        );
    }
}