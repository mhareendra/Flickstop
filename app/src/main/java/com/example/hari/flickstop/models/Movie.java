package com.example.hari.flickstop.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hari on 7/20/2016.
 */
public class Movie implements Parcelable {

    private String posterPath;
    private String originalTitle;
    private String overview;
    private String backdropPath;
    private double rating;
    public MoviePopularity popularity;
    private String releaseDate;
    private long movieID;
    private String originalLanguage;



    protected Movie(Parcel in) {
        backdropPath = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
        movieID = in.readLong();
        originalLanguage = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath()
    {
        return String.format("https://image.tmdb.org/t/p/w780/%s", backdropPath);
    }


    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }

    public boolean isPopular() {
        return (rating > 5);
    }

    public String getReleaseDate()
    {
        return releaseDate;
    }

    public long getMovieID()
    {
        return movieID;
    }

    public String getOriginalLanguage()
    {
        return originalLanguage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getBackdropPath());
        dest.writeString(getOriginalTitle());
        dest.writeString(getOverview());
        dest.writeDouble(getRating());
        dest.writeString(getReleaseDate());
        dest.writeLong(getMovieID());
        dest.writeString(getOriginalLanguage());
    }

    public enum MoviePopularity
    {
        POPULAR, NOT_POPULAR
    }

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.releaseDate = jsonObject.getString("release_date");
        this.movieID = jsonObject.getLong("id");
        this.originalLanguage = jsonObject.getString("original_language");

        this.rating = jsonObject.getDouble("vote_average");
        if(isPopular())
            this.popularity = MoviePopularity.POPULAR;
        else
            this.popularity = MoviePopularity.NOT_POPULAR;
    }

    public static ArrayList<Movie> fromJsonArray(JSONArray array)
    {
        try
        {
            ArrayList<Movie> results = new ArrayList<Movie>();

            for (int i =0 ; i < array.length(); i++)
            {
                JSONObject obj = array.getJSONObject(i);
                results.add(new Movie(obj));
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
