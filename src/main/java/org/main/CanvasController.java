package org.main;

import java.util.ArrayList;
import java.util.List;

public class CanvasController {
    private static List<PrimaryGraph> primaryGraphs = new ArrayList<>();
    private static List<SecondaryGraph> secondaryGraphs = new ArrayList<>();

    public final static String TIME_DOMAIN = "TIME DOMAIN";
    public final static String DIAL = "DIAL";

    public static void addPrimaryGraph(String type, PrimaryGraph graph) {
        switch(type) {
            case (TIME_DOMAIN):
                primaryGraphs.add(new OpenGLTimeDomain(graph.graphX, graph.graphY, graph.graphWidth, graph.graphHeight));
                break;
        }
    }

    private static void addSecondaryGraph(String type, SecondaryGraph graph) {
        switch(type) {
            case (DIAL):
                secondaryGraphs.add(new OpenGLDial(graph.graphX, graph.graphY, graph.graphWidth, graph.graphHeight));
                break;
        }
    }

    public static List<PrimaryGraph> getPrimaryGraphs() {
        return primaryGraphs;
    }

    public static List<SecondaryGraph> getSecondaryGraphs() {
        return secondaryGraphs;
    }
}
