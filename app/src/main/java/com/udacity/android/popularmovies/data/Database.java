package com.udacity.android.popularmovies.data;

import android.net.Uri;

import com.raizlabs.android.dbflow.annotation.provider.ContentProvider;
import com.raizlabs.android.dbflow.annotation.provider.ContentUri;
import com.raizlabs.android.dbflow.annotation.provider.TableEndpoint;

/**
 * Created by jamachad on 06/10/2016.
 */

@ContentProvider(authority = Database.AUTHORITY, database = Database.class, baseContentUri = Database.BASE_CONTENT_URI)
@com.raizlabs.android.dbflow.annotation.Database(name = Database.Name, version = Database.VERSION)
public class Database {
    public static final String Name = "PopularMoviesDB";
    public static final int VERSION = 1;
    public static final String AUTHORITY = "com.udacity.android.popularmovies.provider";

    public static final String BASE_CONTENT_URI = "content://";

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = Uri.parse(Database.BASE_CONTENT_URI + Database.AUTHORITY).buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }


    // Declare endpoints here
    @TableEndpoint(name = MovieProviderModel.ENDPOINT, contentProvider = Database.class)
    public static class MovieProviderModel {

        public static final String ENDPOINT = "Movie";

        @ContentUri(path = MovieProviderModel.ENDPOINT,
                type = ContentUri.ContentType.VND_MULTIPLE + ENDPOINT)
        public static final Uri CONTENT_URI = buildUri(ENDPOINT);
    }
}
