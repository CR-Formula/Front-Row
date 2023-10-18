package org.main;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class DatasetController {
    private static List<Dataset> datasets = new ArrayList<Dataset>();

    public static void addDataset(Dataset dataset) {
        datasets.add(dataset);
    }

    public static List<Dataset> getDatasets() {
        return datasets;
    }

    public static Dataset getDataset(int i) {
        return datasets.get(i);
    }

    public static Dataset removeDataset(int i) {
        return datasets.remove(i);
    }

    public static Dataset removeDataset(String datasetName) {
        try {
            return datasets.remove(findDatasetIndex(datasetName));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Yeah that -1 ain't workin pal");
            throw e;
        }
    }

    private static Dataset findDataset(String datasetName) {
        return datasets.stream().filter(d -> d.getName().equals(datasetName)).findFirst().get();
    }

    private static int findDatasetIndex(String datasetName) {
        for (int i = 0; i < datasets.size(); i++)
            if (datasets.get(i).getName().equals(datasetName))
                return i;

        return -1;
    }

    public static void removeAllDatasets() {
        datasets = new ArrayList<Dataset>();
//        for (Dataset n : datasets)
//            datasets.remove(n);
    }

    public static int getLastSampleIndex() {
        int min = Integer.MAX_VALUE;
        for (Dataset n : datasets)
            min = Math.min(min, n.getLength());

        return min - 1;
    }

    public static void autoDetectDatasets(String port) throws InterruptedException {
        if (!port.equals(DataInput.UART) || !DataInput.isConnected())
            return;

        String[] tokens = DataInput.getLatestTokens();
        while (tokens == null) {
            tokens = DataInput.getLatestTokens();
            Thread.sleep(1);
        }

        Color[] colors = generateRandomColors(tokens.length);
        for (int i = 0; i < tokens.length; i++) {
            addDataset(new Dataset(Integer.toString(i), i, colors[i]));
        }
    }

    public static Color[] generateRandomColors(int length) {
        Color[] colors = new Color[length];
        for (int i = 1; i <= length; i++) {
            float h = (float) i / (length);
            colors[i - 1] = HSLColor.toRGB(h * 360.0f, 100.0f, 50.0f);
        }
        return colors;
    }
}
