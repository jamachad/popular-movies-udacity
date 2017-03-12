package com.udacity.android.popularmovies.Service;

import com.udacity.android.popularmovies.Model.MovieResponse;
import com.udacity.android.popularmovies.Model.ReviewResponse;
import com.udacity.android.popularmovies.Model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by jaime on 12-Mar-17.
 */

public interface ApiService {

    @GET("movie/{id}/reviews")
    Call<ReviewResponse>  loadReviews(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TrailerResponse> loadTrailers(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("http://api.themoviedb.org/3/movie/top_rated")
    Call<MovieResponse> loadTopRatedMovies(@Query("api_key") String apiKey);

    @GET("http://api.themoviedb.org/3/movie/popular")
    Call<MovieResponse> loadPopularMovies(@Query("api_key") String apiKey);
}
