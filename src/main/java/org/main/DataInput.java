package org.main;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.fazecast.jSerialComm.SerialPort;

public class DataInput {
    public static final String TEST = "TEST";
    public static final String UART = "UART";
    private static boolean connected;
    private static String connectionType;

    private static Thread uartThread;
    private static Thread testThread;
    private static Thread[] threads = {testThread, uartThread};

    private static SerialPort uartPort = null;
    private static int uartBaudRate = 115200;

    private static double counter = 0.0001;

    public static void connect(String type) {
        if (type.equals(TEST)) {
            if (connectionType != null && !connectionType.equals(TEST))
                DatasetController.removeAllDatasets();
            connectionType = TEST;
            connected = true;
            startWaveInput();
        } else if (type.equals(UART)) {
            if (connectionType != null && !connectionType.equals(UART))
                DatasetController.removeAllDatasets();
            connectionType = UART;
            connected = true;
            enableUARTConnection();
        }
    }

    public static void disconnect() {
        if (connectionType.equals(TEST)) {
            connected = false;
            stopWaveInput();
        } else if (connectionType.equals(UART)) {
            connected = false;
            disableUARTConnection();
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

    private static void startWaveInput() {
        if (DatasetController.getDatasets().size() == 0) {
            DatasetController.addDataset(new Dataset("sinA", 0, new Color(255, 0, 0)));
            DatasetController.addDataset(new Dataset("sinB", 1, new Color(0, 255, 0)));
            DatasetController.addDataset(new Dataset("sinC", 2, new Color(0, 0, 255)));
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
        if (uartPort != null && uartPort.isOpen())
            uartPort.closePort();

        for (SerialPort port : SerialPort.getCommPorts())
            System.out.println(port.getSystemPortName());

        uartPort = SerialPort.getCommPort(SerialPort.getCommPorts()[0].getSystemPortName());
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
                    String[] tokens = line.split(",");
                    for (String token : tokens) {
                        System.out.print(Float.parseFloat(token) + " ");
                    }
                    System.out.println();

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
        if (uartThread.isAlive())
            uartThread.interrupt();
    }

    public static boolean isConnected() {
        return connected;
    }
}
