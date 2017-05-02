package com.udacity.android.popularmovies.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.udacity.android.popularmovies.R;

/**
 * Created by jaime on 20-Mar-17.
 */

public class MoviesPreferences {
    public static String getMovieOrder(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(context.getString(R.string.pref_listType_key), context.getString(R.string.pref_popular_order));
    }

    public static void setMovieOrder(Context context, String movieOrder){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_listType_key), movieOrder);
        editor.commit();
    }
}
