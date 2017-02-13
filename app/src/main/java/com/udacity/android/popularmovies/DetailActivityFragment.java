package com.udacity.android.popularmovies;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.udacity.android.popularmovies.HelperClasses.Movie;

import java.text.SimpleDateFormat;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    public DetailActivityFragment() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            getActivity().finish();
            return true;
        }else if(item.getItemId() == R.id.action_settings){
            startActivity(new Intent(getActivity(),SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =inflater.inflate(R.layout.fragment_detail, container, false);

        //Movie movie = getArguments().getParcelable("movie");
        Movie movie = getActivity().getIntent().getExtras().getParcelable("movie");
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(movie.originalTitle);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ImageView posterImageView = (ImageView) rootView.findViewById(R.id.posterDetail);

        final Target mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                posterImageView.setImageBitmap(bitmap);
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();
                        if (vibrantSwatch != null) {
                            int mutedColor = vibrantSwatch.getRgb();
                            // If we have a vibrant color
                            // update the title TextView
                            mCollapsingToolbarLayout.setBackgroundColor(mutedColor);
                            //  mutedColor = palette.getMutedColor(R.attr.colorPrimary);
                            mCollapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(mutedColor));
                            mCollapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(mutedColor));

                        }

                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {
                Log.d("DEBUG", "onBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {
                Log.d("DEBUG", "onPrepareLoad");
            }
        };
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w780"+movie.posterPath).into(mTarget);
        Log.d("DEBUG", "http://image.tmdb.org/t/p/w780"+movie.posterPath);
        TextView textViewDescription = (TextView) rootView.findViewById(R.id.descriptionDetail);
        textViewDescription.setText("\n"+movie.overview);
        TextView textViewuserRating = (TextView) rootView.findViewById(R.id.userRatingDetail);
        textViewuserRating.setText(movie.voteAverage.toString());
        TextView textViewuserReleaseDate = (TextView) rootView.findViewById(R.id.releaseDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        textViewuserReleaseDate.setText(simpleDateFormat.format(movie.releaseDate.getTime()));
        return rootView;
    }
}
