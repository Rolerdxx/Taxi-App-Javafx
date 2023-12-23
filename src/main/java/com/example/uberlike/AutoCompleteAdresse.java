package com.example.uberlike;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;



public class AutoCompleteAdresse {
    public static List<String> GET(String in) {
        String apiKey = "AIzaSyCTMx0vIoFllDwiur0k49JglCGCf7ynmAk";

        try {
            String apiUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?" +
                    "input=" + URLEncoder.encode(in, "UTF-8") +
                    "&key=" + apiKey;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();
            List<String> descriptions = parseJson(response.toString());

            return descriptions;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<String> parseJson(String jsonResponse) {
        List<String> descriptions = new ArrayList<>();

        JSONObject json = new JSONObject(jsonResponse);
        JSONArray predictions = json.getJSONArray("predictions");

        for (int i = 0; i < predictions.length(); i++) {
            JSONObject prediction = predictions.getJSONObject(i);
            String description = prediction.getString("description");
            descriptions.add(description);
        }

        return descriptions;
    }
}