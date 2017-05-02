package com.udacity.android.popularmovies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;

import com.udacity.android.popularmovies.model.Movie;
import com.udacity.android.popularmovies.utilities.FirstTimeLoad;
import com.udacity.android.popularmovies.utilities.LoadedMoviesEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity implements PopularMoviesFragment.Callback {

    private Boolean mTwoPane;
    private final String POPULARMOVIESFRAGMENT_TAG = "PMTAG";
    private static final String DETAILMOVIEFRAGMENT_TAG = "DMTAG";
    private boolean mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;

            if (savedInstanceState == null) {
                //getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container, new DetailActivityFragment(), DETAILMOVIEFRAGMENT_TAG).commit();
                getSupportFragmentManager().beginTransaction().add(R.id.container, new PopularMoviesFragment(), POPULARMOVIESFRAGMENT_TAG).commit();
            }
        }
        else{
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
            if (savedInstanceState == null){
                getSupportFragmentManager().beginTransaction().add(R.id.container, new PopularMoviesFragment(),POPULARMOVIESFRAGMENT_TAG).commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFirstTimeLoad(FirstTimeLoad event){
        if(mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable("movie", Parcels.wrap(event.message));

            DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
            detailActivityFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, detailActivityFragment, DETAILMOVIEFRAGMENT_TAG).commit();
        }
    }

    @Override
    public void onMovieItemSelected(Movie movieClicked) {
        if(mTwoPane){
            Bundle args = new Bundle();
            args.putParcelable("movie", Parcels.wrap(movieClicked));

            DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
            detailActivityFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.movie_detail_container, detailActivityFragment, DETAILMOVIEFRAGMENT_TAG).commit();
        }else{
            Intent intentDetailMovie = new Intent(this, DetailActivity.class).putExtra("movie", Parcels.wrap(movieClicked));
            startActivity(intentDetailMovie);
        }
    }
}
