package com.example.hari.flickstop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hari.flickstop.models.Movie;
import com.example.hari.flickstop.models.MovieVideo;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends YouTubeBaseActivity {

    @BindView(R.id.ivPoster) ImageView ivPoster;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.tvReleaseDate) TextView tvReleaseDate;
    @BindView(R.id.tvRating) TextView tvRating;
    @BindView(R.id.tvLanguage) TextView tvLanguage;

    @BindView(R.id.player) YouTubePlayerView youTubePlayerView;

    private long movieID;
    private ArrayList<MovieVideo> movieVideos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        ButterKnife.bind(this);
        Intent intent = this.getIntent();

        if(intent!=null && intent.hasExtra("Movie")) {

            Movie movie = intent.getParcelableExtra("Movie");

            Picasso.with(this).load(movie.getBackdropPath())
                    .placeholder(R.drawable.ic_movie_landscape_placeholder)
                    .transform(new RoundedCornersTransformation(15, 15))
                    .into(ivPoster);

            tvTitle.setText(movie.getOriginalTitle());
            tvOverview.setText(movie.getOverview());
            tvRating.setText(String.valueOf(movie.getRating()));
            tvReleaseDate.setText(movie.getReleaseDate());
            movieID = movie.getMovieID();
            tvLanguage.setText(movie.getOriginalLanguage());
        }

        movieVideos = new ArrayList<>();

        youTubePlayerView.initialize("YOUR API KEY",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        refreshAsyncClient(youTubePlayer);

                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                        Toast.makeText(MovieDetailsActivity.this, "The video could not be played!", Toast.LENGTH_SHORT).show();
                    }
                });


//        ActionBar bar = getSupportActionBar();
//        if (bar!=null) {
//            bar.setDisplayShowHomeEnabled(true);
//            bar.setIcon(R.mipmap.ic_launcher);
//            bar.setDisplayShowTitleEnabled(false);
//        }

    }

    public String getVideoUrl(long movieID)
    {
        return String.format("http://api.themoviedb.org/3/movie/%s/trailers?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed", movieID);
    }

    private void refreshAsyncClient(final YouTubePlayer youTubePlayer)
    {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = getVideoUrl(movieID);
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                refreshList(response);
                if(movieVideos.size() > 0) {
                    String source = movieVideos.get(0).getSource();
                    if(source!=null)
                        youTubePlayer.cueVideo(movieVideos.get(0).getSource());
                    else
                        hideYoutubePlayer();
                }
                else
                    hideYoutubePlayer();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideYoutubePlayer();
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void hideYoutubePlayer()
    {
        ivPoster.setVisibility(View.VISIBLE);
        youTubePlayerView.setVisibility(View.INVISIBLE);
    }

    private void refreshList(JSONObject response)
    {
        try {
            JSONArray resultsArray = null;
            if (response.optJSONArray(("youtube")) != null) {
                resultsArray = response.optJSONArray(("youtube"));
                movieVideos.addAll(MovieVideo.fromJsonArray(resultsArray));
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }


}
