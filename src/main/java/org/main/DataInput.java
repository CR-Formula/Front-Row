package org.main;

import com.fazecast.jSerialComm.SerialPort;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataInput {
//    public enum GraphType {TEST, UART}
    public static final String TEST = "TEST";
    public static final String UART = "UART";
    public static final String CSV = "CSV";
    private static boolean connected;
    private static String connectionType;

    private static Thread uartThread;
    private static Thread testThread;
    private static Thread CSVThread;
    private static Thread[] threads = {testThread, uartThread, CSVThread};

    private static SerialPort uartPort = null;
    private static int uartBaudRate = 115200;
    private static String selectedUARTPort;

    private static double counter = 0.0001;

    private static String[] latestTokens;

    public static File CSVFile;
    public static File configFile;

    public static List<Dataset> referenceDatasets = new ArrayList<>();
    public static int csvElementCount;
    public static final Map<String, Class<?>> stringToGraphMap = new HashMap<>();
    private static double timeInterval;
    private static long startTime;


    public static void connect(String type) {
        switch (type) {
            case TEST -> {
                if (connectionType != null && !connectionType.equals(TEST)){
                    CanvasPanel.instance.resetCanvasPanel();
                    DatasetController.removeAllDatasets();
                }
                connectionType = TEST;
                connected = true;
                startWaveInput();
            }
            case UART -> {
                if (connectionType != null && !connectionType.equals(UART))
                    DatasetController.removeAllDatasets();
                connectionType = UART;
                connected = true;
                enableUARTConnection();
            }
            case CSV -> {
                if (connectionType != null && !connectionType.equals(CSV)){
                    CanvasPanel.instance.resetCanvasPanel();
                    DatasetController.removeAllDatasets();
                }
                connectionType = CSV;
                connected = true;

                if(DatasetController.getDatasets().size() == 0) {
                    readCSVInput();
                    for(Dataset dataset : DatasetController.getDatasets()){
                        referenceDatasets.add(new Dataset(dataset));
                        dataset.getValues().clear();
                    }
                    stringToGraphMap.put(new OpenGLTimeDomain(0,0,0,0).toString(), OpenGLTimeDomain.class);
                    stringToGraphMap.put(new OpenGLDial().toString(), OpenGLDial.class);
                }

                CanvasPanel.instance.readConfig();
                replayCSV();
            }
        }
    }

    public static void disconnect() {
        switch (connectionType) {
            case TEST -> {
                connected = false;
                stopWaveInput();
            }
            case UART -> {
                connected = false;
                disableUARTConnection();
            }
            case CSV -> {
                connected = false;
                disableCSVInput();
            }
        }
    }

    public static void completeDisconnectIfAny() {
        connectionType = "";
        connected = false;

        for (Thread n : threads) {
            if (n != null && n.isAlive()) {
                n.interrupt();
                while (n.isAlive());
            }
        }

        if (DatasetController.getDatasets().size() > 0)
            DatasetController.removeAllDatasets();
    }

    private static void readCSVInput(){
        try (CSVParser parser = CSVParser.parse(CSVFile, Charset.defaultCharset(), CSVFormat.DEFAULT)) {
            int lineCount = 0;
            double timeSum = 0;
            for (CSVRecord record : parser) {
                switch(lineCount){
                    case 0 -> {
                        for(int i = 1; i < record.size(); i++){
                            DatasetController.addDataset(new Dataset(record.get(i), i - 1, new Color(0)));
                        }
                    }
                    case 1 ->{
                        for(int i = 1; i < record.size(); i++){
                            DatasetController.getDataset(i - 1).setLabel(record.get(i));
                        }
                    }
                    case 2 ->{
                        for(int i = 1; i < record.size(); i++){
                            String colorString = record.get(i);

                            String[] rgb = colorString.split(",");
                            int r = Integer.parseInt(rgb[0]);
                            int g = Integer.parseInt(rgb[1]);
                            int b = Integer.parseInt(rgb[2]);

                            DatasetController.getDataset(i - 1).setColor(new Color(r, g, b));
                        }
                    }
                    default -> {
                        timeSum += Double.parseDouble(record.get(0));
                        for(int i = 1; i < record.size(); i++){
                            DatasetController.getDataset(i - 1).add(Float.parseFloat(record.get(i)));
                        }
                    }
                }
                lineCount++;
            }
            timeInterval = timeSum / lineCount - 3;
            csvElementCount = lineCount - 3;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void replayCSV(){
        CSVThread = new Thread(() ->{
//            for(Dataset dataset : DatasetController.getDatasets()){
//                dataset.getValues().clear();
//            }

            for(int k = DatasetController.getDataset(0).getLength(); k < referenceDatasets.get(0).getLength(); k++){
                if(k%10 == 0){
                    JSlider slider = ToolbarPanel.instance.datasetSlider;
                    ChangeListener listener = slider.getChangeListeners()[0];
                    slider.removeChangeListener(listener);
                    slider.setValue((int) (((double)k / csvElementCount) * 100));
                    slider.addChangeListener(listener);
                }
                for(int i = 0; i < referenceDatasets.size(); i++)
                    DatasetController.getDataset(i).add(referenceDatasets.get(i).getValues().get(k));

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        CSVThread.setName("CSV Thread");
        CSVThread.start();
    }

    public static void disableCSVInput() {
        if (CSVThread.isAlive())
            CSVThread.interrupt();
        while (CSVThread.isAlive());
    }

    public static JFileChooser openFileChooser(Container parent){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "csv");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(parent);

        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName());

            return chooser;
        } else {
            System.out.println("Save command cancelled by user.");
            return null;
        }
    }

    private static void startWaveInput() {
        if (DatasetController.getDatasets().size() == 0) {
            Color[] colors = DatasetController.generateRandomColors(3);

            DatasetController.addDataset(new Dataset("sinA", 0, colors[0]));
            DatasetController.addDataset(new Dataset("sinB", 1, colors[1]));
            DatasetController.addDataset(new Dataset("sinC", 2, colors[2]));
        }

        testThread = new Thread(() -> {

            while(true) {
                double[] samples = new double[]{
                        Math.sin(counter),
                        2 * Math.sin(counter * 0.5),
                        5 * Math.sin(counter * 0.2)
                };

                counter += 0.001;

                for (int i = 0; i < DatasetController.getDatasets().size(); i++)
                    DatasetController.getDataset(i).add((float) samples[i]);

                int index = DatasetController.getLastSampleIndex(); // Prints for making sure Thread is alive
//                System.out.println("index: " + index);
//                System.out.printf("sinA: %f\n", DatasetController.getDataset(0).getSample(index));
//                System.out.printf("sinB: %f\n", DatasetController.getDataset(1).getSample(index));
//                System.out.printf("sinC: %f\n", DatasetController.getDataset(2).getSample(index));

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        testThread.setName("Test Output Thread");
        testThread.start();
    }

    private static void stopWaveInput() {
        if (testThread != null && testThread.isAlive()) {
            testThread.interrupt();
            // waits till thread dies - don't want to open another while closing one
            while (testThread.isAlive());
        }
    }

    private static void enableUARTConnection() {
        if (selectedUARTPort == null || selectedUARTPort.isEmpty())
            return;

        if (uartPort != null && uartPort.isOpen())
            uartPort.closePort();

        System.out.println(selectedUARTPort);

        uartPort = SerialPort.getCommPort(selectedUARTPort);
        uartPort.setBaudRate(uartBaudRate);
        uartPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);

        // try 3 times before giving up
        if (!uartPort.openPort()) {
            if (!uartPort.openPort()) {
                if (!uartPort.openPort()) {
                    System.out.println("Unable to connect to UART port.");
                    return;
                }
            }
        }

        connected = true;

        startReceivingUARTData(uartPort.getInputStream());
    }

    private static void startReceivingUARTData(InputStream stream) {
        uartThread = new Thread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            while(true) {
                try {
                    if (!reader.ready()) uartThread.sleep(1);

                    String line = reader.readLine();
                    latestTokens = line.split(",");
                    for (Dataset dataset : DatasetController.getDatasets()) {
                        dataset.add(Float.parseFloat(latestTokens[dataset.getIndex()]));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    return;
                }
            }
        });

        uartThread.setPriority(Thread.MAX_PRIORITY);
        uartThread.setName("UART Input Thread");
        uartThread.start();
    }

    private static void disableUARTConnection() {
        latestTokens = null;
        if (uartThread.isAlive())
            uartThread.interrupt();
        while (uartThread.isAlive());
    }

    public static boolean isConnected() {
        return connected;
    }

    public static String[] getLatestTokens() {
        return latestTokens;
    }

    public static String[] getOpenUARTPorts() {
        SerialPort[] openSerialPorts = SerialPort.getCommPorts();
        String[] openPortNames = new String[openSerialPorts.length];
        for (int i = 0; i < openSerialPorts.length; i++)
            openPortNames[i] = openSerialPorts[i].getSystemPortName();

        return openPortNames;
    }

    public static void setUARTPort(String portName) {
        selectedUARTPort = portName;
    }

    public static String connectionType() {
        return selectedUARTPort.equals(TEST) ? TEST : UART;
    }

    public static long getStartTime(){return startTime;}
    public static void setStartTime(long time){startTime = time;}
}
