package com.example.uberlike;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GoogleMapsGeocoding {

    private static final String API_KEY = "AIzaSyCTMx0vIoFllDwiur0k49JglCGCf7ynmAk";

    public static String GET(String address) {

        try {
            String encodedAddress = URLEncoder.encode(address, "UTF-8");
            String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?" +
                    "address=" + encodedAddress +
                    "&key=" + API_KEY;

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

            return parseCoordinates(response.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String parseCoordinates(String jsonResponse) {

        String lat = "";
        String lng = "";

        if (jsonResponse != null && !jsonResponse.isEmpty()) {
            int latIndex = jsonResponse.indexOf("\"lat\"") + 7;
            int lngIndex = jsonResponse.indexOf("\"lng\"") + 7;

            if (latIndex > 0 && lngIndex > 0) {
                lat = jsonResponse.substring(latIndex, jsonResponse.indexOf(',', latIndex));
                lng = jsonResponse.substring(lngIndex, jsonResponse.indexOf('}', lngIndex));
            }
        }

        return lat + ", " + lng;
    }
}
