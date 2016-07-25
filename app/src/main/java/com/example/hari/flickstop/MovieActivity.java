package com.example.hari.flickstop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.hari.flickstop.adapters.MovieArrayAdapter;
import com.example.hari.flickstop.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {


    ArrayList<Movie> movies;

    MovieArrayAdapter movieAdapter;

    @BindView(R.id.lvMovies) ListView lvMovies;

    private SwipeRefreshLayout swipeContainer;

    private String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ButterKnife.bind(this);

        client = new AsyncHttpClient();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAsyncClient();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_red_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvMovies.setAdapter(movieAdapter);

        refreshAsyncClient();

        ActionBar bar = getSupportActionBar();
        if (bar!=null) {
            bar.setDisplayShowHomeEnabled(true);
            bar.setIcon(R.mipmap.ic_launcher);
            bar.setDisplayShowTitleEnabled(false);
        }
    }


    @OnItemClick(R.id.lvMovies)
    protected void ListItemClick(int position)
    {
        if(movies.get(position).popularity == Movie.MoviePopularity.POPULAR)
        {
            Movie movie = movies.get(position);
            Intent videoPlayerIntent = new Intent(this,
                    VideoPlayerActivity.class)
                    .putExtra("movieID", movie.getMovieID());
            startActivity(videoPlayerIntent);
        }
        else
        {
            startDetailsActivity(position);
        }
    }

    @OnItemLongClick(R.id.lvMovies)
    protected boolean ListItemLongClick(int position)
    {
        startDetailsActivity(position);
        return true;
    }

    private void refreshAsyncClient()
    {

        client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                refreshList(response);
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void refreshList(JSONObject response)
    {
        try {
            JSONArray resultsArray = null;

            if (response.optJSONArray(("results")) != null) {
                movieAdapter.clear();
                movieAdapter.notifyDataSetChanged();
                resultsArray = response.optJSONArray(("results"));
                movies.addAll(Movie.fromJsonArray(resultsArray));
                movieAdapter.notifyDataSetChanged();
                //Log.d("Movies", movies.toString());
            }
            //Log.d("results", resultsArray.toString());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void startDetailsActivity(int position)
    {
        Movie movie = movies.get(position);
        Intent detailIntent = new Intent(this,
                MovieDetailsActivity.class)
                .putExtra("Movie", movie);

        startActivity(detailIntent);
    }
}
