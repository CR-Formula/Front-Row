package org.main;


import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Theme {
    private static org.json.simple.JSONObject simpleJSONObject;
    private static org.json.JSONObject JSONObject;

    public static void main(String[] args) throws IOException, ParseException {
        loadConfig();
    }
    public Theme() throws IOException, ParseException {
        loadConfig();
    }

    private static void loadConfig() throws IOException {
        // TODO: Put each json info into respective global variables

        JSONParser parser = new JSONParser();
        try {
            Object configObject = parser.parse(new FileReader(".\\src\\main\\resources\\theme_config.json"));

        } catch (ParseException e) {
            System.out.println("MISSING FILE: theme_config.json");
        }

    }
}
