package com.udacity.android.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.support.v7.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {
    private final String DETAIL_ACTIVITY_FRAGMENT_TAG = "DetailActivityFragment_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(savedInstanceState == null){
           getSupportFragmentManager().beginTransaction().add(R.id.container, new DetailActivityFragment(),DETAIL_ACTIVITY_FRAGMENT_TAG).commit();

        }
    }


}
