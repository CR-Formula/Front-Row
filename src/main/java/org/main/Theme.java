package org.main;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Theme {
    // Paddings
    public static int graphPadding = 10;

    // Colors
    public static Color pageBackground = new Color(0x504f61);
    public static Color graphBackground = new Color(0x000000);
    public static Color graphBorder = new Color(0xFFFFFF);

    // Font details
    public static String fontName = "Courier";
    public static Font largeFont = new Font(fontName, Font.PLAIN, 16);
    public static Font normalFont = new Font(fontName, Font.PLAIN, 12);
    public static Font smallFont = new Font(fontName, Font.PLAIN, 8);

    // Line details
    public static int smallLineWidth = 1;
    public static int mediumLineWidth = 3;
    public static int largeLineWidth = 15;

    public static void main(String[] args) throws IOException {
        loadConfig();
    }
    public Theme() throws IOException {
//        loadConfig();
    }

    private static void loadConfig() throws IOException {
        // TODO: Put each json info into respective global variables
        // new File(".\\src\\main\\resources\\theme_config.json")
//        System.out.println(graphPadding);
//        ObjectMapper objectMapper = new ObjectMapper();
////        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        File file = new File("src\\main\\resources\\theme_config.json");
//        System.out.println(file.getAbsoluteFile());
//        objectMapper.readValue(file, Theme.class);
//        System.out.println(graphPadding);
    }
}
