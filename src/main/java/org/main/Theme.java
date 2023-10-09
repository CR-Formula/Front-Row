package org.main;

import com.google.gson.annotations.Expose;

import java.awt.*;

public class Theme {
    // Paddings
    public static int graphPadding = 10;

    // Colors
    public final static Color pageBackground = new Color(0x504f61);
    public final static Color graphBackground = new Color(0x000000);
    public final static Color graphBorder = new Color(0xFFFFFF);
    public final static Color graphInfo = new Color(0xFFD677);

    // Font details
    public final static String fontName = "Courier";
    public final  static Font largeFont = new Font(fontName, Font.PLAIN, 16);
    public final  static Font normalFont = new Font(fontName, Font.PLAIN, 12);
    public final  static Font smallFont = new Font(fontName, Font.PLAIN, 8);

    // Line details
    public final  static int smallLineWidth = 1;
    public final  static int mediumLineWidth = 3;
    public final  static int largeLineWidth = 15;
}
