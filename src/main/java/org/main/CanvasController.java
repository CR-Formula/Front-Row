package org.main;

import java.util.ArrayList;
import java.util.List;

public class CanvasController {
    private static List<Graph> graphs = new ArrayList<>();
    private static List<PrimaryGraph> primaryGraphs = new ArrayList<>();
    private static List<SecondaryGraph> secondaryGraphs = new ArrayList<>();

    private final static String TIME_DOMAIN = "TIME DOMAIN";
    private final static String DIAL = "DIAL";

    public static void createPrimaryGraph(String type) {
        switch(type) {
            case (TIME_DOMAIN):
                break;
        }

    }

    private static void createSecondaryGraph(String type) {
        switch(type) {
            case (DIAL):
                break;
        }
    }



}
