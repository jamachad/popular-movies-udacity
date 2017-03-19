package com.udacity.android.popularmovies.Sync;

import android.content.Context;
import android.content.Intent;

/**
 * Created by jamachad on 19/03/2017.
 */

public class SyncUtils {
    public static void startImmediateSync(Context context){
        Intent intentToSyncImmediately = new Intent(context, SyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
