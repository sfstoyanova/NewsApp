package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Viper on 10-May-18.
 */

public class QueryUtils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public QueryUtils() {
    }

    /**
     * Query the USGS dataset and return an {@link New} object to represent a single earthquake.
     */
    public static ArrayList<New> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link New} object
        ArrayList<New> newsList = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return newsList;
    }

    /**
     * Return an {@link New} object by parsing out information
     * about the first New from the input JSON string.
     */
    private static ArrayList<New> extractFeatureFromJson(String earthquakeJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        //Create an empty list of news
        ArrayList<New> newsList = new ArrayList<>();

        try {

            //parse the JSON response to JSONObject
            JSONObject jsonObject = new JSONObject(earthquakeJSON);

            //Finds and get the response from JSONObject with key "response"
            JSONObject jsonResponse = jsonObject.getJSONObject("response");

            //Finds and get the array from JSONArray with key "results"
            JSONArray jsonResults = jsonResponse.getJSONArray("results");

            for (int i = 0; i < jsonResults.length(); i++) {
                //Create a JSON Object which holds current object from jsonResults
                JSONObject news = jsonResults.getJSONObject(i);
                //Create a JSON Array ssociated with key tags
                JSONArray tags = news.getJSONArray("tags");

                //Extract the value associated with key webTitle
                String title = news.getString("webTitle");
                //Extract the value associated with key webPublicationDate
                String publishDate = news.getString("webPublicationDate");
                //Extract the value associated with key sectionName
                String section = news.getString("sectionName");
                //Extract the value associated with key webUrl
                String webSite = news.getString("webUrl");

                String author = "";

                if (tags.length() != 0 ) {
                    JSONObject object = tags.getJSONObject(0);
                    //Extract the value associated with key webTitle
                    author = object.getString("webTitle");
                }

                newsList.add(new New(title, section, publishDate, webSite, author));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        return newsList;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }

        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == urlConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }
}

