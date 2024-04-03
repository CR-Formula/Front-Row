package org.main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
    public JSlider datasetSlider;
    private JButton exportButton;
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
        int optionsLength = openPorts.length + 2;
        String[] options = new String[optionsLength];
        options[0] = DataInput.TEST;
        options[1] = DataInput.CSV;

        for (int i = 2; i < options.length; i++) {
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
        SpinnerNumberModel leftModel = new SpinnerNumberModel(PrimaryGraph.getLeftMargin(), 0.00, 1.00, 0.05);
        leftMarginSpinner = new JSpinner(leftModel);
        leftMarginSpinner.setMaximumSize(new Dimension((int) (leftMarginSpinner.getPreferredSize().getWidth()  * 1.5), (int) leftMarginSpinner.getPreferredSize().getHeight()));
        leftMarginSpinner.addChangeListener(e -> {
            PrimaryGraph.setLeftMargin((Double) leftMarginSpinner.getValue());
        });

        bottomMarginLabel = new JLabel("Bottom Margin: ");
        bottomMarginLabel.setForeground(Theme.fontColor);
        SpinnerNumberModel bottomModel = new SpinnerNumberModel(PrimaryGraph.getBottomMargin(), 0.00, 1.00, 0.05);
        bottomMarginSpinner = new JSpinner(bottomModel);
        bottomMarginSpinner.setMaximumSize(new Dimension((int) (bottomMarginSpinner.getPreferredSize().getWidth()  * 1.5), (int) bottomMarginSpinner.getPreferredSize().getHeight()));
        bottomMarginSpinner.addChangeListener(e -> {
            PrimaryGraph.setBottomMargin((Double) bottomMarginSpinner.getValue());
        });
      
        inputTypeOptions = new JComboBox<>(options);
        Dimension comboBoxAMaxSize = new Dimension((int) (inputTypeOptions.getPreferredSize().getWidth()  * 1.1), (int) inputTypeOptions.getPreferredSize().getHeight());
        inputTypeOptions.setMaximumSize(comboBoxAMaxSize);
        inputTypeOptions.addActionListener(event -> {
            selectedOption = inputTypeOptions.getSelectedItem().toString();
        });

        connectButton = new JButton("Connect");
        connectButton.addActionListener(event -> {
            switch (selectedOption) {
                case DataInput.TEST -> {
                    DataInput.connect(selectedOption);
                    DataInput.setStartTime(System.currentTimeMillis());
                }
                case DataInput.UART -> {
                    DataInput.setUARTPort(selectedOption);
                    DataInput.connect(DataInput.UART);
                    DataInput.setStartTime(System.currentTimeMillis());
                    try {
                        DatasetController.autoDetectDatasets(DataInput.UART);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                case DataInput.CSV -> {

                    PanelManager.instance.replaceComponent(CanvasPanel.instance, BorderLayout.CENTER);
                    PanelManager.instance.removeComponent(CanvasPanel.instance);

                    JFileChooser chooser = DataInput.openFileChooser(getParent());
                    if(chooser != null) DataInput.CSVFile = chooser.getSelectedFile();

                    chooser = DataInput.openFileChooser(getParent());
                    if(chooser != null){
                        DataInput.configFile = chooser.getSelectedFile();
                        datasetSlider.setVisible(true);
                        DataInput.connect(selectedOption);
                        DataInput.setStartTime(System.currentTimeMillis());
                    }
                }
            }

            if(DataInput.isConnected()){
                JScrollPane scrollPane = new JScrollPane(DatasetPanel.instance);
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                PanelManager.instance.replaceComponent(scrollPane, BorderLayout.CENTER);
                layout.replace(connectButton, continueButton);
            }
        });

        continueButton = new JButton("Continue");
        continueButton.addActionListener(event -> {
            if (!DataInput.isConnected()) DataInput.connect(selectedOption);
            DatasetPanel.confirmChanges();
            PanelManager.instance.replaceComponent(CanvasPanel.instance, BorderLayout.CENTER);
            layout.replace(continueButton, disconnectButton);

//            exportButton.setVisible(true);
        });

        disconnectButton = new JButton("Disconnect");
        disconnectButton.addActionListener(event -> {
            DataInput.disconnect();
            PanelManager.instance.replaceComponent(DatasetPanel.instance, BorderLayout.CENTER);
            layout.replace(disconnectButton, continueButton);
        });

        exportButton = new JButton("Export");
        exportButton.addActionListener(event -> {
            try {
                exportToCSV();
            } catch (Exception e) {
                System.err.println("Export failed1. Error: " + e.getMessage());
            }
        });

        datasetSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        datasetSlider.setMajorTickSpacing(20);
        datasetSlider.setMinorTickSpacing(5);
        datasetSlider.setPaintTicks(true);
        datasetSlider.setPaintLabels(true);
        datasetSlider.setMaximumSize(new Dimension((int) (datasetSlider.getPreferredSize().getWidth()  * 1.5), 5));
        ChangeListener changeListener = e -> {
            DataInput.disableCSVInput();

            if(datasetSlider.getValue() > 0){
                double index = ((double) datasetSlider.getValue() / 100) * DataInput.csvElementCount;
                for(int i = 0; i < DatasetController.getDatasets().size(); i++){
                    List<Float> newValues = DataInput.referenceDatasets.get(i).getValues().subList(0, (int) index);
                    DatasetController.getDataset(i).getValues().clear();
                    DatasetController.getDataset(i).getValues().addAll(newValues);
                }
            }

            DataInput.replayCSV();
        };
        datasetSlider.addChangeListener(changeListener);
        datasetSlider.setVisible(false);


        add(exportButton);
        add(dimensionLabel);
        add(dimensionSelector);
        add(leftMarginLabel);
        add(leftMarginSpinner);
        add(bottomMarginLabel);
        add(bottomMarginSpinner);
        add(inputTypeOptions);
        add(connectButton);
        add(datasetSlider);

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(exportButton)
                        .addGap(Theme.toolbarPadding)
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
                        .addComponent(datasetSlider)
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
                        .addComponent(exportButton)
                        .addComponent(dimensionLabel)
                        .addComponent(dimensionSelector)
                        .addComponent(leftMarginLabel)
                        .addComponent(leftMarginSpinner)
                        .addComponent(bottomMarginLabel)
                        .addComponent(bottomMarginSpinner)
                        .addComponent(datasetSlider)
                        .addComponent(inputTypeOptions)
                        .addComponent(connectButton)
        );
    }

    private void exportToCSV() {
        JFileChooser chooser = DataInput.openFileChooser(getParent());

        String location = null;
        if(chooser != null){
            DataInput.CSVFile = chooser.getSelectedFile();
            location = chooser.getSelectedFile().toString();
        }

        try (PrintWriter writer = new PrintWriter(location)) {
            writer.print("Timestamp (Second)");
            for (Dataset dataset : DatasetController.getDatasets()) {
                writer.print("," + dataset.getName());
            }
            writer.println();
            for (Dataset dataset : DatasetController.getDatasets()) {
                writer.print("," + dataset.getLabel());
            }
            writer.println();
            for (Dataset dataset : DatasetController.getDatasets()) {
                writer.print(",\"" + dataset.getColor().getRed() + "," + dataset.getColor().getGreen() + "," + dataset.getColor().getBlue() + "\"");
            }
            writer.println();

            int maximumLength = getMaximumLength((ArrayList<Dataset>) DatasetController.getDatasets());
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - DataInput.getStartTime();
            double increment = (double) elapsedTime / maximumLength;
            double timestamp = 0;

            for (int i = 0; i < maximumLength; i++) {
                writer.print((timestamp+=increment) / 1000);

                for (Dataset dataset : DatasetController.getDatasets()) {
                    if (i < dataset.getLength()) {
                        writer.print("," + dataset.getSample(i));
                    } else {
                        writer.print(",");
                    }
                }

                writer.println();
            }

        } catch (Exception e) {
            System.err.println("Export failed2. Error: " + e.getMessage());
        }

        chooser = DataInput.openFileChooser(getParent());

        if(chooser != null){
            DataInput.configFile = chooser.getSelectedFile();
            location = chooser.getSelectedFile().toString();
        }

        try (PrintWriter writer = new PrintWriter(location)) {
            CanvasPanel.instance.saveConfig(writer);
        } catch (Exception e) {
            System.err.println("Export failed2. Error: " + e.getMessage());
        }
    }

    private int getMaximumLength(ArrayList<Dataset> datasets) {
        int max = 0;
        for (Dataset dataset : datasets) {
            max = Math.max(max, dataset.getLength());
        }
        return max;
    }
}