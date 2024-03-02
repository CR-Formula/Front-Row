package org.main;

import com.jogamp.opengl.GL2ES3;

import java.awt.*;

public class Theme {
    // Paddings
    public static int graphPadding = 8;
    public static int toolbarPadding = 10;
    public static int datasetRowPadding = 5;
    public static int datasetRowHorizontalPadding = 10;
    public static int datasetRowVerticalPadding = 5;
    public static int maxDatasetRowHorizontalPadding = 50;
    public static double graphMinPadding = 0.01;

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
    //    public final static Font largeFont = new Font(fontName, Font.PLAIN, 16);
//    public final static Font normalFont = new Font(fontName, Font.PLAIN, 12);
//    public final static Font smallFont = new Font(fontName, Font.PLAIN, 8);
    public final static Color fontColor = new Color(0xFFFFFF);

    // Line details
    public final static int smallLineWidth = 1;
    public final static int mediumLineWidth = 3;
    public final static int largeLineWidth = 15;

    // Canvas Panel Layers
    public final static int BaseLayer = 0;
    public final static int GraphLayer = 1;
    public final static int GraphTextLayer = 2;

    // general swing
//    public static Color jpanelColor         = new JPanel().getBackground();
//    public static int   padding             = Integer.parseInt(System.getProperty("java.version").split("\\.")[0]) >= 9 ? 5 : (int) (5 * ChartsController.getDisplayScalingFactor());
    public static Color defaultDatasetColor = Color.RED;

    // general opengl
    public static float lineWidth = 1.0f;
    public static float pointWidth = 3.0f;

    // charts region
    public static float[] tileColor               = new float[] {0.8f, 0.8f, 0.8f, 1.0f};
    public static float[] tileShadowColor         = new float[] {0.7f, 0.7f, 0.7f, 1.0f};
    public static float[] tileSelectedColor       = new float[] {0.5f, 0.5f, 0.5f, 1.0f};
    public static float   tilePadding             = 5.0f;
    public static float   tileShadowOffset        = tilePadding / 2;
//    public static float[] neutralColor            = new float[] {jpanelColor.getRed() / 255.0f, jpanelColor.getGreen() / 255.0f, jpanelColor.getBlue() / 255.0f, 1.0f};
//    public static float[] transparentNeutralColor = new float[] {neutralColor[0], neutralColor[1], neutralColor[2], 0.7f};

    // plot region
    public static float[] plotOutlineColor        = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
    //    public static float[] plotBackgroundColor     = neutralColor;
    public static float[] divisionLinesColor      = new float[] {0.7f, 0.7f, 0.7f, 1.0f};
    public static float[] divisionLinesFadedColor = new float[] {0.7f, 0.7f, 0.7f, 0.0f};

    // tooltips and markers in the plot region
    public static float[] tooltipBackgroundColor  = new float[] {1, 1, 1, 1};
    public static float[] tooltipBorderColor      = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
    public static float[] markerBorderColor       = new float[] {0.6f, 0.6f, 0.6f, 1.0f};
    public static float[] tooltipVerticalBarColor = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
    public static float   tooltipTextPadding      = 5.0f;

    // tick marks surrounding the plot region
    public static float[] tickLinesColor   = new float[] {0.0f, 0.0f, 0.0f, 1.0f};
    public static float   tickLength       = 6.0f;
    public static float   tickTextPadding  = 3.0f;

    // legend
    public static float[] legendBackgroundColor = tileShadowColor;
    public static float   legendTextPadding     = 5.0f;
    public static float   legendNamesPadding    = 25.0f;

    // fonts
    public static Font smallFont  = new Font("Geneva", Font.PLAIN, 12);
    public static Font mediumFont = new Font("Geneva", Font.BOLD,  14);
    public static Font largeFont  = new Font("Geneva", Font.BOLD,  18);

    public static void initialize(GL2ES3 gl, float displayScalingFactor) {
        lineWidth  = displayScalingFactor;
        pointWidth = 3.0f * displayScalingFactor;

        tilePadding      = 5.0f * displayScalingFactor;
        tileShadowOffset = tilePadding / 2;

        tooltipTextPadding = 5.0f * displayScalingFactor;

        tickLength      = 6.0f * displayScalingFactor;
        tickTextPadding = 3.0f * displayScalingFactor;

        legendTextPadding  = 5.0f * displayScalingFactor;
        legendNamesPadding = 25.0f * displayScalingFactor;

        smallFont  = new Font("Geneva", Font.PLAIN, (int) (12.0 * displayScalingFactor));
        mediumFont = new Font("Geneva", Font.BOLD,  (int) (14.0 * displayScalingFactor));
        largeFont  = new Font("Geneva", Font.BOLD,  (int) (18.0 * displayScalingFactor));
        OpenGL.updateFontTextures(gl);
    }
}

