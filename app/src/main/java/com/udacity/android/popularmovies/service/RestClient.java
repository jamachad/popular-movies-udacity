package com.udacity.android.popularmovies.service;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.udacity.android.popularmovies.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jaime on 12-Mar-17.
 */

public class RestClient {
    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String API_KEY = BuildConfig.THE_MOVIE_DB_API_KEY;
    private ApiService apiService;

    public RestClient()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public ApiService getApiService()
    {
        return apiService;
    }
}
