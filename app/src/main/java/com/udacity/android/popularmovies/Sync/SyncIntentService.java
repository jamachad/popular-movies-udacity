package com.udacity.android.popularmovies.Sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by jamachad on 19/03/2017.
 */

public class SyncIntentService extends IntentService{
    public SyncIntentService() {
        super(SyncIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SyncTask.syncMovies(this);
    }
}
