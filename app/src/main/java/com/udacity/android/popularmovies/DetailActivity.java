package com.udacity.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.support.v7.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {
    private final static String DETAILMOVIEFRAGMENT_TAG = "DMTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        if(savedInstanceState == null){
            Bundle arguments = new Bundle();
            arguments.putParcelable("movie", getIntent().getExtras().getParcelable("movie"));

            DetailActivityFragment detailActivityFragment = new DetailActivityFragment();
            detailActivityFragment.setArguments(arguments);
           getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container, detailActivityFragment ,DETAILMOVIEFRAGMENT_TAG).commit();

        }
    }


}
