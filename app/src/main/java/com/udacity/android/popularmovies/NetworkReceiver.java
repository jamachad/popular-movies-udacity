package com.udacity.android.popularmovies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.udacity.android.popularmovies.utilities.InternetChangeEvent;
import com.udacity.android.popularmovies.utilities.LoadedMoviesEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by jaime on 01-May-17.
 */

public class NetworkReceiver extends BroadcastReceiver {
    private final EventBus eventBus = EventBus.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected() && activeNetwork.isAvailable()) {
            eventBus.post(new InternetChangeEvent(true));
        }else {
            eventBus.post(new InternetChangeEvent(false));
        }
    }
}
