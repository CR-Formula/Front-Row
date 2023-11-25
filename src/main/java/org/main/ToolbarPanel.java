package org.main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ToolbarPanel extends JPanel {

    public static ToolbarPanel instance = new ToolbarPanel();
    private GroupLayout layout;
    private JLabel dimensionLabel;
    private JSpinner dimensionSelector;
    private JButton connectButton;
    private JButton continueButton;
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

        dimensionLabel = new JLabel("Rows: ");
        dimensionLabel.setForeground(Theme.fontColor);
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel((int) CanvasPanel.instance.canvasDimension.getHeight(), 0, (int) CanvasPanel.instance.canvasDimension.getHeight(), 1);
        dimensionSelector = new JSpinner(spinnerModel);
        dimensionSelector.setMaximumSize(dimensionSelector.getPreferredSize());
        dimensionSelector.addChangeListener(e -> {
            CanvasPanel.instance.setCanvasDimension((Integer) dimensionSelector.getValue());
        });

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
                try {
                    DatasetController.autoDetectDatasets(DataInput.UART);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            JScrollPane scrollPane = new JScrollPane(DatasetPanel.instance);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            PanelManager.instance.replaceComponent(scrollPane, BorderLayout.CENTER);
            layout.replace(connectButton, continueButton);
        });

        continueButton = new JButton("Continue");
        continueButton.addActionListener(event -> {
            if (!DataInput.isConnected()) DataInput.connect(selectedOption);
            PanelManager.instance.replaceComponent(CanvasPanel.instance, BorderLayout.CENTER);
            layout.replace(continueButton, disconnectButton);
        });

        disconnectButton = new JButton("Disconnect");
        disconnectButton.addActionListener(event -> {
            DataInput.disconnect();
            PanelManager.instance.replaceComponent(DatasetPanel.instance, BorderLayout.CENTER);
            layout.replace(disconnectButton, continueButton);
        });

        add(dimensionLabel);
        add(dimensionSelector);
        add(inputTypeOptions);
        add(connectButton);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(dimensionLabel)
                        .addComponent(dimensionSelector)
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
                        .addComponent(dimensionLabel)
                        .addComponent(dimensionSelector)
                        .addComponent(inputTypeOptions)
                        .addComponent(connectButton)
        );
    }
}