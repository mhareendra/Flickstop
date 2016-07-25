package com.example.hari.flickstop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.left_drawer) ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;

    private SwipeRefreshLayout swipeContainer;

    private String urlNowPlaying = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private String urlTopRated = "https://api.themoviedb.org/3/movie/top_rated?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private String urlUpcoming = "https://api.themoviedb.org/3/movie/upcoming?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private String urlPopular = "https://api.themoviedb.org/3/movie/popular?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private AsyncHttpClient client;
    private String mActivityTitle;

    private String currentUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ButterKnife.bind(this);

        mActivityTitle = getTitle().toString();

        addDrawer();
        setupDrawer();
        currentUrl = urlNowPlaying;
        client = new AsyncHttpClient();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAsyncClient(currentUrl);
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


        refreshAsyncClient(urlNowPlaying);

        ActionBar bar = getSupportActionBar();
        if (bar!=null) {
            bar.setDisplayShowHomeEnabled(true);
            bar.setIcon(R.mipmap.ic_launcher);
            bar.setDisplayShowTitleEnabled(false);
            bar.setHomeButtonEnabled(true);
        }

    }

    private void addDrawer()
    {
        String[] navDrawListItems = new String[]{"Now Playing", "Upcoming", "Top Rated", "Popular"};

        final ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, navDrawListItems);

        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                navigationDrawerClickListener(mAdapter.getItem(position));
            }
        });
    }

    private void navigationDrawerClickListener(String item)
    {
        if(item.equals("Top Rated"))
        {
            refreshAsyncClient(urlTopRated);
            currentUrl = urlTopRated;
        }
        else if(item.equals("Upcoming"))
        {
            refreshAsyncClient(urlUpcoming);
            currentUrl = urlUpcoming;
        }
        else if(item.equals("Now Playing"))
        {
            refreshAsyncClient(urlNowPlaying);
            currentUrl = urlNowPlaying;
        }
        else if(item.equals("Popular"))
        {
            refreshAsyncClient(urlPopular);
            currentUrl = urlPopular;
        }
        else {
            return;
        }
        if(mDrawerLayout!=null)
            mDrawerLayout.closeDrawer(mDrawerList);

    }

    private void setupDrawer()
    {
        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {

                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
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

    private void refreshAsyncClient(String url)
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
