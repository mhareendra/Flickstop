package com.example.hari.flickstop.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hari.flickstop.R;
import com.example.hari.flickstop.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Hari on 7/22/2016.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {


    public MovieArrayAdapter(Context context, List<Movie> movies)
    {
        super(context, 0, movies);
    }

    private static class ViewHolder {
        ImageView movieImage;
        TextView title;
        TextView overview;
    }

    private static class ViewHolderPopular {
        ImageView movieImage;
    }


    // Returns the number of types of Views that will be created by getView(int, View, ViewGroup)
    @Override
    public int getViewTypeCount() {
        return Movie.MoviePopularity.values().length;
    }

    // Get the type of View that will be created by getView(int, View, ViewGroup)
    // for the specified item.
    @Override
    public int getItemViewType(int position) {
        return (getItem(position).popularity.ordinal());
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        Movie movie = getItem(position);
        int viewType = getItemViewType(position);

        try {

            //Don't base the layout on the movie's popularity when in landscape mode
            if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                convertView = getViewNonPopularMovie(convertView, movie);
                ImageView youtubeIcon = (ImageView) convertView.findViewById(R.id.ivYoutubeIcon);
                if(movie.popularity == Movie.MoviePopularity.POPULAR)
                {
                    youtubeIcon.setVisibility(View.VISIBLE);
                }
            }
            else {
                if (viewType == Movie.MoviePopularity.POPULAR.ordinal()) {
                    convertView = getViewPopularMovie(convertView, movie);
                } else {
                    convertView = getViewNonPopularMovie(convertView, movie);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return convertView;
    }

//    public View getView(int position, View convertView, ViewGroup parent)
//    {
//        Movie movie = getItem(position);
//        ViewHolder viewHolder;
//        ViewHolderPopular viewHolderPopular;
//
//        if(convertView == null)
//        {
//            viewHolder = new ViewHolder();
//            convertView = getInflatedLayoutForType(getItemViewType(position));
//            //LayoutInflater inflater = LayoutInflater.from(getContext());
//           // convertView = inflater.inflate(R.layout.item_movie, parent, false);
//            viewHolder.movieImage = (ImageView)convertView.findViewById(R.id.ivMovie);
//            viewHolder.title = (TextView)convertView.findViewById(R.id.tvMovieTitle);
//            viewHolder.overview = (TextView)convertView.findViewById(R.id.tvMovieOverview);
//            convertView.setTag(viewHolder);
//        }
//        else
//        {
//            viewHolder = (ViewHolder)convertView.getTag();
//        }
//
//        viewHolder.overview.setText(movie.getOverview());
//        viewHolder.title.setText(movie.getOriginalTitle());
//
//
//        int orientation = getContext().getResources().getConfiguration().orientation;
//        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//
//            Picasso.with(getContext()).load(movie.getPosterPath())
//                    .transform(new RoundedCornersTransformation(10, 10))
//                    .placeholder(R.drawable.ic_movie_placeholder)
//                    .into(viewHolder.movieImage);
//        }
//        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
//        {
//            Picasso.with(getContext()).load(movie.getBackdropPath())
//                    .transform(new RoundedCornersTransformation(10, 10))
//                    .placeholder(R.drawable.ic_movie_placeholder)
//                    .into(viewHolder.movieImage);
//        }
//
//
//        return convertView;
//    }


    private View getViewNonPopularMovie( View convertView, Movie movie)
    {
        ViewHolder viewHolder;
        if(convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
            viewHolder.movieImage = (ImageView)convertView.findViewById(R.id.ivMovie);
            viewHolder.title = (TextView)convertView.findViewById(R.id.tvMovieTitle);
            viewHolder.overview = (TextView)convertView.findViewById(R.id.tvMovieOverview);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.overview.setText(movie.getOverview());
        viewHolder.title.setText(movie.getOriginalTitle());


        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            Picasso.with(getContext()).load(movie.getPosterPath())
                    .transform(new RoundedCornersTransformation(10, 10))
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .into(viewHolder.movieImage);
        }
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            Picasso.with(getContext()).load(movie.getBackdropPath())
                    .transform(new RoundedCornersTransformation(10, 10))
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .into(viewHolder.movieImage);
        }
        return convertView;
    }

    private View getViewPopularMovie(View convertView, Movie movie)
    {
        ViewHolderPopular viewHolderPopular;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie_popular, null);
            viewHolderPopular = new ViewHolderPopular();

            viewHolderPopular.movieImage = (ImageView)convertView.findViewById(R.id.ivMovie);
            convertView.setTag(viewHolderPopular);
        }
        else
        {
            viewHolderPopular = (ViewHolderPopular) convertView.getTag();
        }

        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {

            Picasso.with(getContext()).load(movie.getBackdropPath())
                    .transform(new RoundedCornersTransformation(10, 10))
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .into(viewHolderPopular.movieImage);
        }
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            Picasso.with(getContext()).load(movie.getBackdropPath())
                    .transform(new RoundedCornersTransformation(10, 10))
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .into(viewHolderPopular.movieImage);
        }

        return convertView;
    }



}
