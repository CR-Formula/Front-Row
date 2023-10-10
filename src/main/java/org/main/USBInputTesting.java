package org.main;

public class USBInputTesting {

    public static void main(String[] args) throws InterruptedException {
        DataInput.connect(DataInput.UART);
        DatasetController.autoDetectDatasets(DataInput.UART);
    }
}
