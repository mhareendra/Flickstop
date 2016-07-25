package com.example.hari.flickstop.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hari on 7/24/2016.
 */
public class MovieVideo {


    public String getSource() {
        return source;
    }

    public String getName() {
        return name;
    }

    private String name;
    private String source;

    public MovieVideo(JSONObject jsonObject) throws JSONException
    {
        this.name = jsonObject.getString("name");
        this.source = jsonObject.getString("source");

    }


    public static ArrayList<MovieVideo> fromJsonArray(JSONArray array)
    {
        try
        {
            ArrayList<MovieVideo> results = new ArrayList<>();

            for (int i =0 ; i < array.length(); i++)
            {
                JSONObject obj = array.getJSONObject(i);
                results.add(new MovieVideo(obj));
            }
            return results;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
}
