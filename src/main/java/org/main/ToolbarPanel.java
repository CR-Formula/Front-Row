package org.main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ToolbarPanel extends JPanel {

    public static ToolbarPanel instance = new ToolbarPanel();
    private GroupLayout layout;
    private JLabel dimensionLabel;
    private JSpinner dimensionSelector;
    private JLabel leftMarginLabel;
    private JSpinner leftMarginSpinner;
    private JLabel bottomMarginLabel;
    private JSpinner bottomMarginSpinner;
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
        SpinnerNumberModel dimensionModel = new SpinnerNumberModel((int) CanvasPanel.instance.canvasDimension.getHeight(), 0, 99, 1);
        dimensionSelector = new JSpinner(dimensionModel);
        dimensionSelector.setMaximumSize(dimensionSelector.getPreferredSize());
        dimensionSelector.addChangeListener(e -> {
            CanvasPanel.instance.setCanvasDimension((Integer) dimensionSelector.getValue());
        });

        leftMarginLabel = new JLabel("Left Margin: ");
        leftMarginLabel.setForeground(Theme.fontColor);
        SpinnerNumberModel leftModel = new SpinnerNumberModel(OpenGLTimeDomain.getLeftMargin(), 0.00, 1.00, 0.05);
        leftMarginSpinner = new JSpinner(leftModel);
        leftMarginSpinner.setMaximumSize(new Dimension((int) (leftMarginSpinner.getPreferredSize().getWidth()  * 1.5), (int) leftMarginSpinner.getPreferredSize().getHeight()));
        leftMarginSpinner.addChangeListener(e -> {
            OpenGLTimeDomain.setLeftMargin((Double) leftMarginSpinner.getValue());
        });

        bottomMarginLabel = new JLabel("Bottom Margin: ");
        bottomMarginLabel.setForeground(Theme.fontColor);
        SpinnerNumberModel bottomModel = new SpinnerNumberModel(OpenGLTimeDomain.getBottomMargin(), 0.00, 1.00, 0.05);
        bottomMarginSpinner = new JSpinner(bottomModel);
        bottomMarginSpinner.setMaximumSize(new Dimension((int) (bottomMarginSpinner.getPreferredSize().getWidth()  * 1.5), (int) bottomMarginSpinner.getPreferredSize().getHeight()));
        bottomMarginSpinner.addChangeListener(e -> {
            OpenGLTimeDomain.setBottomMargin((Double) bottomMarginSpinner.getValue());
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
            DatasetPanel.confirmChanges();
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
        add(leftMarginLabel);
        add(leftMarginSpinner);
        add(bottomMarginLabel);
        add(bottomMarginSpinner);
        add(inputTypeOptions);
        add(connectButton);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(dimensionLabel)
                        .addComponent(dimensionSelector)
                        .addGap(Theme.toolbarPadding)
                        .addComponent(leftMarginLabel)
                        .addComponent(leftMarginSpinner)
                        .addGap(Theme.toolbarPadding)
                        .addComponent(bottomMarginLabel)
                        .addComponent(bottomMarginSpinner)
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
                        .addComponent(leftMarginLabel)
                        .addComponent(leftMarginSpinner)
                        .addComponent(bottomMarginLabel)
                        .addComponent(bottomMarginSpinner)
                        .addComponent(inputTypeOptions)
                        .addComponent(connectButton)
        );
    }
}