package com.driver.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyGeocoder {

//Method will contact the Google Geocoder API and return a JSONObject containing
//a list of Address data
//https://developers.google.com/maps/documentation/geocoding/
//Method may return null
public static String getLocationInfo(double latitude, double longitude) {

    String coords = (float) latitude + "," + (float) longitude;
    String getUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + coords;
    URL url = null;

    HttpURLConnection urlConnection = null;
    BufferedReader br = null;

    StringBuilder stringBuilder = null;
    int statusCode = 0;

    try {
        url = new URL(getUrl);

        urlConnection = (HttpURLConnection) url.openConnection();
        // set the connection timeout to 10 seconds and the read timeout to 120 seconds
        urlConnection.setConnectTimeout(10 * 1000);
        urlConnection.setReadTimeout(120 * 1000);
        urlConnection.connect();

        InputStream stream = new BufferedInputStream(urlConnection.getInputStream());

        statusCode = urlConnection.getResponseCode();

        InputStreamReader streamReader = new InputStreamReader(stream);
        br = new BufferedReader(streamReader);

        String line = "";
        stringBuilder = new StringBuilder();

        while ((line = br.readLine()) != null) {
            stringBuilder.append(line);
        }
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException ex) {
        ex.printStackTrace();
    } finally {
        //close buffered reader and under lying stream
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
    }

    Log.d("MyGeocoder", "statusCode = " + statusCode + " HTTPStatus.SC_OK = " + HttpURLConnection.HTTP_OK);

    //create json object from result string and then parse it
    if (statusCode == HttpURLConnection.HTTP_OK) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.e("MyGeocoder", "JSONException e = " + e.getMessage());
            e.printStackTrace();
        }

        return parseJSONAddressList(jsonObject);
    } else {
        return null;
    }
}


//Method will parse JSONObject and return a List<Address> containing one Address.
//The Address will only contain data for county, state and country.
public static String parseJSONAddressList(JSONObject jsonObject) {

    Log.d("MyGeocoder", "In parseJSONAddressList");

    String addressData=null;


    if (jsonObject != null) {
        try {
            //get and iterate through the results array


            JSONArray results = jsonObject.getJSONArray("results");

            JSONObject jsonObject1=results.getJSONObject(0);
            addressData  = jsonObject1.getString("formatted_address");

            return addressData;

        } catch (JSONException e) {
            Log.e("MyGeocoder", "parseJSONAddressList JSONException e = " + e.getMessage());
            e.printStackTrace();
        }
    }
    return null;
  }
}