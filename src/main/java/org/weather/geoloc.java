package org.weather;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class geoloc {
    private static final String BASE_URL = "http://api.openweathermap.org/geo/1.0/";
    private static final String API_KEY = "f897a99d971b5eef57be6fafa0d83239";
    static HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

    public static Map<String, String> getGeoLocation(String location) throws Exception {


        boolean isZipCode = false;
        String city;
        String state;
        URI fullUrl = null;
        if (!Character.isDigit(location.charAt(0))) {

            if (location.contains(",")){
                String[] parts = location.split(",");
                city = parts[0];
                city = city.replaceAll(" ", "%20");
                state = parts[1].strip();
                state = state.replaceAll(" ", "%20");
                fullUrl = URI.create(BASE_URL + "direct" + "?q=" + city + "," + state + ",US&appid=" + API_KEY);
            } else {
                fullUrl = URI.create(BASE_URL + "direct" + "?q=" + location + ",US&appid=" + API_KEY);
            }

        } else {
            String zipCode = location.trim();
            fullUrl = URI.create(BASE_URL + "zip" + "?zip=" + zipCode + ",US&appid=" + API_KEY);
            isZipCode = true;
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(fullUrl)
                .headers("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = httpResponse.body();

        JSONObject res;
        try {
            if (isZipCode) {
                res = new JSONObject(responseBody);
            } else {
                JSONArray jsonArray = new JSONArray(responseBody);
                res = jsonArray.getJSONObject(0);

            }
        } catch (JSONException e) {
            throw new JSONException("Invalid location entered: " + location);
        }
        if (res.keySet().contains("cod")) {
            throw new JSONException("Invalid location entered: " + location);
        }

        if (!isZipCode) {
            return Map.of(
                    "City", res.get("name").toString(),
                    "latitude", res.get("lat").toString(),
                    "longitude", res.get("lon").toString(),
                    "state", res.get("state").toString()
            );
        }
        return Map.of(
                "City", res.get("name").toString(),
                "latitude", res.get("lat").toString(),
                "longitude", res.get("lon").toString()
        );
    }
}
