package org.main;

import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InitialGraphPanel extends JPanel {
    public enum GraphType {PRIMARY, SECONDARY}
    private ArrayList<Dataset> graphDatasets = new ArrayList<>();
    private JFrame popupFrame;
    private GraphType graphType;
    private Graph graph;
    private GroupLayout layout;
    private JPanel container;
    private GLJPanel graphPanel;
    private JLabel datasetLabel;
    private JLabel graphLabel;

    private JCheckBox timeDomain;
    private JCheckBox dial;

    public InitialGraphPanel(JPanel container, GLJPanel graphPanel, GraphType type){
        super();

        this.container = container;
        this.graphPanel = graphPanel;
        this.graphType = type;

        datasetLabel = new JLabel("Datasets");
        graphLabel = new JLabel("Graph Type");

        layout = new GroupLayout(this);

        layout.setAutoCreateGaps(false);
        layout.setAutoCreateContainerGaps(false);
        setLayout(layout);
        setBackground(Theme.canvasBackground);

        GroupLayout.ParallelGroup horizontalDatasetGroup = layout.createParallelGroup();
        GroupLayout.SequentialGroup verticalDatasetGroup = layout.createSequentialGroup();
        horizontalDatasetGroup.addComponent(datasetLabel);
        verticalDatasetGroup.addComponent(datasetLabel);

        GroupLayout.ParallelGroup horizontalGraphTypeGroup = layout.createParallelGroup();
        GroupLayout.SequentialGroup verticalGraphTypeGroup = layout.createSequentialGroup();
        verticalGraphTypeGroup.addComponent(graphLabel);
        horizontalGraphTypeGroup.addComponent(graphLabel);


        if(graphType == GraphType.PRIMARY){
            for(Dataset dataset : DatasetController.getDatasets()){
                String label = dataset.getLabel().isEmpty() ? dataset.getName() : dataset.getLabel();
                JCheckBox jCheckBox = new JCheckBox(label);
                jCheckBox.addActionListener(event -> {
                    if(jCheckBox.isSelected()) graphDatasets.add(dataset);
                    else graphDatasets.remove(dataset);
                });
                horizontalDatasetGroup.addComponent(jCheckBox);
                verticalDatasetGroup.addComponent(jCheckBox);
            }

            timeDomain = new JCheckBox("Time Domain");
            timeDomain.addActionListener(event  -> graph = new OpenGLTimeDomain(0, 0, 0, 0));

            verticalGraphTypeGroup.addComponent(timeDomain);
            horizontalGraphTypeGroup.addComponent(timeDomain);
        }
        else if (graphType  == GraphType.SECONDARY){
            int datasetArrSize = DatasetController.getDatasets().size();
            Dataset[] datasetArr = new Dataset[datasetArrSize];
            for(int i = 0; i < datasetArrSize; i++) datasetArr[i] = DatasetController.getDatasets().get(i);

            JComboBox<Dataset> datasetComboBox = new JComboBox<>(datasetArr);
            Dimension comboBoxAMaxSize = new Dimension((int) (datasetComboBox.getPreferredSize().getWidth()  * 1.1), (int) datasetComboBox.getPreferredSize().getHeight());
            datasetComboBox.setMaximumSize(comboBoxAMaxSize);
            graphDatasets.add((Dataset) datasetComboBox.getSelectedItem());
            datasetComboBox.addActionListener(event -> {
                graphDatasets.clear();
                graphDatasets.add((Dataset) datasetComboBox.getSelectedItem());
            });

            horizontalDatasetGroup.addComponent(datasetComboBox);
            verticalDatasetGroup.addComponent(datasetComboBox);

            dial = new JCheckBox("Dial");
            dial.addActionListener(event  -> graph = new OpenGLDial(0, 0, 0, 0));

            verticalGraphTypeGroup.addComponent(dial);
            horizontalGraphTypeGroup.addComponent(dial);
        }

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED,
                                GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
                        .addGroup(horizontalGraphTypeGroup)
                        .addGap(Theme.datasetRowPadding)
                        .addGroup(horizontalDatasetGroup)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED,
                                GroupLayout.DEFAULT_SIZE, Integer.MAX_VALUE)
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup().addGap(Theme.datasetRowVerticalPadding).addGroup(
                        layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addGroup(verticalGraphTypeGroup)
                                .addGroup(verticalDatasetGroup)
                )
        );

        buildFrame();
    }

    public void confirmChanges(){
        if(graphType == GraphType.SECONDARY){
            ((SecondaryGraph) graph).setDataset(graphDatasets.get(0));
            graph.toggleAutoDetectMaxMin();
        }
        else {
            graph.setDatasets(graphDatasets);
            graph.toggleAutoDetectMaxMin();
        }

        graphPanel.addGLEventListener(graph);
        graphPanel.addMouseListener(graph);

        Animator animator = new Animator(graphPanel);
        animator.setUpdateFPSFrames(1, null);
        animator.start();

        graphPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        container.removeAll();
        container.add(graphPanel, BorderLayout.CENTER);
        CanvasPanel.instance.setupCanvasLayout();

        popupFrame.dispose();
    }

    public void hideFrame(){
        popupFrame.dispose();
    }

    public void buildFrame(){
        popupFrame = new JFrame("Initialize Graph");
        BorderLayout layoutFrame = new BorderLayout();
        popupFrame.setLayout(layoutFrame);

        Dimension parentFrameDimension = CanvasPanel.instance.getParent().getSize();
        Dimension popupDimension = new Dimension((int) (parentFrameDimension.getWidth() / 2), (int) (parentFrameDimension.getHeight() / 2));
        popupFrame.setSize(popupDimension);

        Point parentFrameLocation = CanvasPanel.instance.getParent().getLocation();
        Point popupLocation = new Point((int) (parentFrameLocation.getX() + (parentFrameDimension.getWidth() / 2) - (popupDimension.getWidth() / 2)), (int) (parentFrameLocation.getY() + (parentFrameDimension.getHeight() / 2) - (popupDimension.getHeight() / 2)));

        popupFrame.setLocation(popupLocation);

        JScrollPane scrollPane = new JScrollPane(this);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        popupFrame.add(scrollPane, BorderLayout.CENTER);
        popupFrame.add(new InitialGraphToolbarPanel(this), BorderLayout.SOUTH);
        popupFrame.setVisible(true);
    }

}