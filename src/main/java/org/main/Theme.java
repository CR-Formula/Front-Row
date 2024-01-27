package org.main;

import java.awt.*;

public class Theme {
    // Paddings
    public static int graphPadding = 8;
    public static int toolbarPadding = 10;
    public static int datasetRowPadding = 5;
    public static int datasetRowHorizontalPadding = 10;
    public static int datasetRowVerticalPadding = 5;
    public static int maxDatasetRowHorizontalPadding = 50;

    // Colors
    public final static Color canvasBackground = new Color(0x6C6B7C);
    public final static Color toolbarBackground = new Color(0x464552);
    public final static Color graphBackground = new Color(0x000000);
    public final static Color graphBorder = new Color(0xFFFFFF);
    public final static Color datasetRowBackground = new Color(0xBEBEBE);
    public final static Color graphInfo = new Color(0xFFD677);
    public final static Color blankGraphColor = new Color(0xBDBDBD);

    // Font details
    public final static String fontName = "Courier"; // Other fonts to look at "DialogInput", "Dialog", "Serif"
    public final static Font largeFont = new Font(fontName, Font.PLAIN, 16);
    public final static Font normalFont = new Font(fontName, Font.PLAIN, 12);
    public final static Font smallFont = new Font(fontName, Font.PLAIN, 8);
    public final static Color fontColor = new Color(0xFFFFFF);

    // Line details
    public final static int smallLineWidth = 1;
    public final static int mediumLineWidth = 3;
    public final static int largeLineWidth = 15;
}
