package com.udacity.android.popularmovies.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jaime on 12-Mar-17.
 */

public class RestClient {
    private static final String BASE_URL = "http://api.themoviedb.org/3/";
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
