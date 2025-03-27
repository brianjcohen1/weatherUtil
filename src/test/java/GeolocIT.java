import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.weather.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Test
public class GeolocIT {

    /*
    Test cases for GeolocUtil:
        1. Single valid City,State combo
        2. Single valid zip code
        3. List of City,State + zip code, multiple cities, multiple zip codes
        4. Invalid Entries
        5. Assorted error handling

    */

    private JSONObject PhiladelphiaResponseObject;
    JSONObject PhiladelphiaZipResponseObject;
    JSONObject NewYorkResponseObject;

    @BeforeTest(alwaysRun = true)
    public void setup() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();

        String PhiladelphiaUri = "http://api.openweathermap.org/geo/1.0/direct?q=Philadelphia,PA,US&limit=1&appid=f897a99d971b5eef57be6fafa0d83239";
        String PhiladelphiaZipUri = "http://api.openweathermap.org/geo/1.0/zip?zip=19104&limit=1&appid=f897a99d971b5eef57be6fafa0d83239";
        String NewYorkUri = "http://api.openweathermap.org/geo/1.0/direct?q=New%20York,NY,US&limit=1&appid=f897a99d971b5eef57be6fafa0d83239";
        String invalidZipUri = "http://api.openweathermap.org/geo/1.0/zip?zip=190&limit=1&appid=f897a99d971b5eef57be6fafa0d83239";

        HttpResponse<String> PhiladelphiaResponse = client.send(HttpRequest.newBuilder().uri(URI.create(PhiladelphiaUri)).build(), HttpResponse.BodyHandlers.ofString());
        JSONArray PhiladelphiaResponseArray = new JSONArray(PhiladelphiaResponse.body());
        PhiladelphiaResponseObject = PhiladelphiaResponseArray.getJSONObject(0);

        HttpResponse<String> PhiladelphiaZipResponse = client.send(HttpRequest.newBuilder().uri(URI.create(PhiladelphiaZipUri)).build(), HttpResponse.BodyHandlers.ofString());
        PhiladelphiaZipResponseObject = new JSONObject(PhiladelphiaZipResponse.body());

        HttpResponse<String> NewYorkResponse = client.send(HttpRequest.newBuilder().uri(URI.create(NewYorkUri)).build(), HttpResponse.BodyHandlers.ofString());
        JSONArray NewYorkResponseArray = new JSONArray(NewYorkResponse.body());
        NewYorkResponseObject = NewYorkResponseArray.getJSONObject(0);
    }

    // Basic usage of the utility - supply a single city name and a single zip code, confirm that the utility returns
    // the expected values
    @Test
    public void testSingleCity() throws IOException, InterruptedException {

        GeolocUtil geolocUtil = new GeolocUtil();
        Map<String, String> geolocPhiladelphia = geolocUtil.geolocUtil("Philadelphia, PA");
        assert geolocPhiladelphia.get("City").equals(PhiladelphiaResponseObject.get("name"));
        assert geolocPhiladelphia.get("latitude").equals(PhiladelphiaResponseObject.get("lat").toString());
        assert geolocPhiladelphia.get("longitude").equals(PhiladelphiaResponseObject.get("lon").toString());

        Map<String, String> geoloc19104 = geolocUtil.geolocUtil("19104");
        assert geoloc19104.get("City").equals(PhiladelphiaZipResponseObject.get("name"));
        assert geoloc19104.get("latitude").equals(PhiladelphiaZipResponseObject.get("lat").toString());
        assert geoloc19104.get("longitude").equals(PhiladelphiaZipResponseObject.get("lon").toString());
    }

    //Supply multiple entries to the utility, both city names and a zip code. Confirm that all fields are
    //returned as expected
    @Test
    public void testMultiCity() throws IOException, InterruptedException {
        GeolocUtil geolocUtil = new GeolocUtil();
        List<String> locations = Arrays.asList("Philadelphia, PA", "19104", "New York, NY");
        List<Map<String, String>> geolocList = geolocUtil.geolocUtil(locations);
        assert geolocList.get(0).get("City").equals(PhiladelphiaResponseObject.get("name"));
        assert geolocList.get(0).get("state").equals(PhiladelphiaResponseObject.get("state"));
        assert geolocList.get(0).get("latitude").equals(PhiladelphiaResponseObject.get("lat").toString());
        assert geolocList.get(0).get("longitude").equals(PhiladelphiaResponseObject.get("lon").toString());
        assert geolocList.get(1).get("City").equals(PhiladelphiaZipResponseObject.get("name").toString());
        assert geolocList.get(1).get("latitude").equals(PhiladelphiaZipResponseObject.get("lat").toString());
        assert geolocList.get(1).get("longitude").equals(PhiladelphiaZipResponseObject.get("lon").toString());
        assert geolocList.get(2).get("City").equals(NewYorkResponseObject.get("name"));
        assert geolocList.get(2).get("latitude").equals(NewYorkResponseObject.get("lat").toString());
        assert geolocList.get(2).get("longitude").equals(NewYorkResponseObject.get("lon").toString());
    }

    @Test
    public void testInvalidEntries() throws IOException, InterruptedException {
        GeolocUtil geolocUtil = new GeolocUtil();
        try {
            Map<String, String> invalidCity = geolocUtil.geolocUtil("Invalid City, State");
            assert false;
        } catch (Exception e) {
            assert e.getMessage().contains("Invalid location");
        }
        try {
            Map<String, String> invalidZip = geolocUtil.geolocUtil("190");
        } catch (Exception e) {
            assert e.getMessage().contains("Invalid location");
        }

        //Invalid city, valid zip code - should throw an error due to invalid location
        List<String> locations = Arrays.asList("Invalid City, State", "19104");
        try {
            List<Map<String, String>> geolocList = geolocUtil.geolocUtil(locations);
        } catch (Exception e) {
            assert e.getMessage().contains("Invalid location");
        }

        //Valid city, invalid zip code - should throw an error due to invalid location
        List<String> locationsInvalidZip = Arrays.asList("Philadelphia,PA", "191");
        try {
            List<Map<String, String>> geolocList = geolocUtil.geolocUtil(locationsInvalidZip);
        } catch (Exception e) {
            assert e.getMessage().contains("Invalid location");
        }

    }


}
