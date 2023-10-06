package org.main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Theme {
    public static void main(String[] args) throws IOException, ParseException {
        loadConfig();
    }
    public Theme() throws IOException, ParseException {
        loadConfig();
    }

    private static void loadConfig() throws IOException, ParseException {
        String filePath = new File("src/main/java/resources/theme_config.json").getAbsolutePath();

        JSONParser parser = new JSONParser();
        try {
            Object configObject = parser.parse(new FileReader("./src/main/java/resources/theme_config.json"));

            JSONObject config =  (JSONObject) configObject;
            System.out.println(config.toString());
        } catch (Exception e) {
            System.out.println(filePath + " bruh");
        }
    }
}
