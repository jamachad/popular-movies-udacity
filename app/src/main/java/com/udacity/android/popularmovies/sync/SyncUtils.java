package com.udacity.android.popularmovies.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.udacity.android.popularmovies.model.Database;
import com.udacity.android.popularmovies.model.Movie_Table;

/**
 * Created by jamachad on 19/03/2017.
 */

public class SyncUtils {

    private static boolean sInitialized;

    synchronized public static void initialize(@NonNull final Context context){
        if(sInitialized) return;

        sInitialized = true;

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Uri moviesUri = Database.MovieProviderModel.CONTENT_URI;
                String[] projectionColums = {Movie_Table.id.getNameAlias().getNameAsKey()};

                Cursor cursor = context.getContentResolver().query(
                        moviesUri,
                        projectionColums,
                        null,
                        null,
                        null);

                if(cursor==null || cursor.getCount()==0){
                    startImmediateSync(context);
                }
                cursor.close();
                return null;
            }
        }.execute();
    }

    public static void startImmediateSync(Context context){
        Intent intentToSyncImmediately = new Intent(context, SyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
