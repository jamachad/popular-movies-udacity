package com.udacity.android.popularmovies.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.udacity.android.popularmovies.R;
import com.udacity.android.popularmovies.utilities.LoadedMoviesEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Created by jamachad on 19/03/2017.
 */

public class SyncIntentService extends IntentService{
    public SyncIntentService() {
        super(SyncIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            EventBus.getDefault().post(new LoadedMoviesEvent(View.VISIBLE));
            SyncTask.syncMovies(this, getString(R.string.pref_popular_order));
            SyncTask.syncMovies(this, getString(R.string.pref_top_rated_order));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            EventBus.getDefault().post(new LoadedMoviesEvent(View.INVISIBLE));
        }
    }
}
