package org.weather;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeolocUtil {
    public List<Map<String, String>> geolocUtil (List<String> locations) {

        geoloc geoloc = new geoloc();

        List<Map<String, String>> geoLocList = new ArrayList<>();
        for (String location : locations) {
            try {
                location = location.replaceAll(" ", "%20");
                geoLocList.add(geoloc.getGeoLocation(location));
            } catch (Exception e) {
                throw new JSONException("Invalid location entered: " + location);
            }
        }
        return geoLocList;
    }

    public Map<String, String> geolocUtil (String location) {

        geoloc geoloc = new geoloc();
        Map<String, String> geolocMap = null;
        try {
            geolocMap = geoloc.getGeoLocation(location);
        } catch (Exception e) {
            throw new JSONException("Invalid location entered: " + location);
        }

        return geolocMap;
    }
}
