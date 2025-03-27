package org.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String BASE_URL = "http://api.openweathermap.org/geo/1.0/";
    private static final String API_KEY = "f897a99d971b5eef57be6fafa0d83239";
    static HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();


    public static void main(String[] args) throws IOException, InterruptedException {


        List<String> locations = Arrays.asList(args);

        GeolocUtil geolocUtil = new GeolocUtil();
        System.out.println(geolocUtil.geolocUtil(locations));


    }






}