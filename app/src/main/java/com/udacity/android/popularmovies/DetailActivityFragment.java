package com.udacity.android.popularmovies;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.udacity.android.popularmovies.model.Database;
import com.udacity.android.popularmovies.model.Movie;
import com.udacity.android.popularmovies.model.Movie_Table;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment{

    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private String YOUTUBE_API_KEY = "AIzaSyAQGcXavGY2YTSSsJIOLj_slulGLBkrsTk";
    private Movie mMovie;


    public DetailActivityFragment() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            getActivity().finish();
            return true;
        }else if(item.getItemId() == R.id.sort_by_popular_movies){
            startActivity(new Intent(getActivity(),SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =inflater.inflate(R.layout.fragment_movie, container, false);

        //Movie movie = getArguments().getParcelable("movie");
        mMovie = Parcels.unwrap(getActivity().getIntent().getExtras().getParcelable("movie"));
        //Movie movie = getActivity().getIntent().getExtras().getParcelable("movie");
       /* mCollapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(mMovie.title);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/
        if(mMovie.getTrailers().size() > 0) {
            final YouTubeThumbnailView youTubeThumbnailView = (YouTubeThumbnailView) rootView.findViewById(R.id.youtube_thumbnail);
            youTubeThumbnailView.initialize(YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(mMovie.getTrailers().get(0).getKey());
                    //youTubeThumbnailLoader.release();
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });

            youTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(YouTubeStandalonePlayer.createVideoIntent(getActivity(),
                            "AIzaSyAQGcXavGY2YTSSsJIOLj_slulGLBkrsTk", mMovie.getTrailers().get(0).getKey(), 0, true, true));
                }
            });
        }



        final ImageView posterImageView = (ImageView) rootView.findViewById(R.id.posterDetail);


        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w780"+mMovie.getPosterPath()).into(posterImageView);
        Log.d("DEBUG", "http://image.tmdb.org/t/p/w780"+mMovie.getPosterPath());

        TextView textViewMovieTitle = (TextView) rootView.findViewById(R.id.textViewMovieTitle);
        textViewMovieTitle.setText(mMovie.getTitle());

        TextView textViewDescription = (TextView) rootView.findViewById(R.id.descriptionDetail);
        textViewDescription.setText("\n"+mMovie.overview);
        TextView textViewuserRating = (TextView) rootView.findViewById(R.id.userRatingDetail);
        textViewuserRating.setText(mMovie.voteAverage.toString());
        TextView textViewuserReleaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
        textViewuserReleaseDate.setText(mMovie.getReleaseDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);
        if(mMovie.getFavoriteMovie() == 0) fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMovie.getFavoriteMovie() == 0){
                    mMovie.setFavoriteMovie(1);
                    fab.setImageResource(R.drawable.ic_favorite_black_24dp);
                }else if(mMovie.getFavoriteMovie() == 1){
                    mMovie.setFavoriteMovie(0);
                    fab.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }
                SQLite.update(Movie.class)
                        .set(Movie_Table.favoriteMovie.eq(mMovie.getFavoriteMovie()))
                        .where(Movie_Table.id.is(mMovie.getId()))
                        .execute();
                getActivity().getContentResolver().notifyChange(Database.MovieProviderModel.CONTENT_URI,  null);
            }
        });

        return rootView;
    }

}
