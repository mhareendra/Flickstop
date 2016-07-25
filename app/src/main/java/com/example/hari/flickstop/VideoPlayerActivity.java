package com.example.hari.flickstop;

import android.os.Bundle;
import android.widget.Toast;

import com.example.hari.flickstop.models.MovieVideo;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class VideoPlayerActivity extends YouTubeBaseActivity {

    private long movieID;
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        movieID = this.getIntent().getLongExtra("movieID", 0);

        youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

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
                        Toast.makeText(VideoPlayerActivity.this, "The video could not be played!", Toast.LENGTH_SHORT).show();
                    }
                });


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
                String source = refreshList(response);
                if (source != null) {
                    youTubePlayer.setFullscreen(true);
                    youTubePlayer.setShowFullscreenButton(false);
                    youTubePlayer.loadVideo(source);
                }
                else
                    return;

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private String refreshList(JSONObject response)
    {
        if(response == null)
            return null;
        try {

            JSONArray resultsArray = null;
            if (response.optJSONArray(("youtube")) != null) {
                resultsArray = response.optJSONArray(("youtube"));
                ArrayList<MovieVideo> mv = MovieVideo.fromJsonArray(resultsArray);

                if(mv != null && (!mv.isEmpty()))
                    return mv.get(0).getSource();
                else
                    return null;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

}
