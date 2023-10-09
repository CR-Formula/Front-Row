package org.main;

import java.awt.*;

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
}
