package org.main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.xml.crypto.Data;
import java.awt.*;

public class ToolbarPanel extends JPanel {

    public static ToolbarPanel instance = new ToolbarPanel();
    private GroupLayout layout;
    private JButton connectButton;
    private JButton disconnectButton;
    private JComboBox<String> inputTypeOptions;
    private String selectedOption = DataInput.TEST;

    private ToolbarPanel() {
        super();

        layout = new GroupLayout(this);

        setLayout(layout);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Theme.toolbarBackground);

        layout.setAutoCreateGaps(false);
        layout.setAutoCreateContainerGaps(false);

        String[] openPorts = DataInput.getOpenUARTPorts();
        int optionsLength = openPorts.length + 1;
        String[] options = new String[optionsLength];
        options[0] = DataInput.TEST;

        for (int i = 1; i < options.length; i++) {
            options[i] = openPorts[i-1];
        }

        inputTypeOptions = new JComboBox<>(options);
        Dimension comboBoxAMaxSize = new Dimension((int) (inputTypeOptions.getPreferredSize().getWidth()  * 1.1), (int) inputTypeOptions.getPreferredSize().getHeight());
        inputTypeOptions.setMaximumSize(comboBoxAMaxSize);
        inputTypeOptions.addActionListener(event -> {
            selectedOption = inputTypeOptions.getSelectedItem().toString();
        });

        connectButton = new JButton("Connect");
        connectButton.addActionListener(event -> {
            if (selectedOption.equals(DataInput.TEST)) {
                DataInput.connect(selectedOption);
            } else {
                DataInput.setUARTPort(selectedOption);
                DataInput.connect(DataInput.UART);
            }
            layout.replace(connectButton, disconnectButton);
        });

        disconnectButton = new JButton("Disconnect");
        disconnectButton.addActionListener(event -> {
            DataInput.disconnect();
            layout.replace(disconnectButton, connectButton);
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