package com.udacity.android.popularmovies.sync;

import android.content.Context;
import android.util.Log;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.udacity.android.popularmovies.R;
import com.udacity.android.popularmovies.model.Database;
import com.udacity.android.popularmovies.model.Movie;
import com.udacity.android.popularmovies.model.MovieResponse;
import com.udacity.android.popularmovies.model.Movie_Table;
import com.udacity.android.popularmovies.model.Review;
import com.udacity.android.popularmovies.model.ReviewResponse;
import com.udacity.android.popularmovies.model.Trailer;
import com.udacity.android.popularmovies.model.TrailerResponse;
import com.udacity.android.popularmovies.service.ApiService;
import com.udacity.android.popularmovies.service.RestClient;
import com.udacity.android.popularmovies.utilities.MoviesPreferences;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jamachad on 19/03/2017.
 */

public class SyncTask {
    static final String LOG_TAG = SyncTask.class.getSimpleName();

    synchronized public static boolean syncMovies(final Context context, String movieOrder) throws IOException {
        final RestClient restClient = new RestClient();
        Response<MovieResponse> movieResponse = null;
        if (movieOrder.equals(context.getString(R.string.pref_popular_order))) {
            movieResponse = restClient.getApiService().loadPopularMovies(RestClient.API_KEY).execute();
        } else if (movieOrder.equals(context.getString(R.string.pref_top_rated_order))) {
            movieResponse = restClient.getApiService().loadTopRatedMovies(RestClient.API_KEY).execute();
        }
        if(movieResponse.body() != null) {
            List<Movie> movies = movieResponse.body().getMovies();
            Log.d(LOG_TAG, "Number of movies received: " + movies.size());
            for (Movie movie : movies
                    ) {
                Movie returnedMovie = new Select().from(Movie.class)
                        .where(Movie_Table.id.eq(movie.id)).querySingle();
                movie.movie_order = movieOrder;
                if (returnedMovie != null) {
                    movie.setFavoriteMovie(returnedMovie.getFavoriteMovie());
                    if (!returnedMovie.movie_order.equals(movie.movie_order)) {
                        movie.movie_order = context.getString(R.string.PopularAndTopRated_Movie);
                    }
                }
                movie.save();
                syncTrailer(context, movie, restClient);
                syncReviews(context, movie, restClient);
            }
            return true;
        }else{
            return false;
        }
    }

    synchronized public static void syncTrailer(Context context, Movie movie, RestClient restClient) throws IOException {
        Response<TrailerResponse> trailerResponse = restClient.getApiService().loadTrailers(String.valueOf(movie.getId()), RestClient.API_KEY).execute();
        if (trailerResponse.body()  != null) {
                   List<Trailer> trailers = trailerResponse.body().getTrailers();
                    Log.d(LOG_TAG, "Number of trailers received: " + trailers.size());
                    for (Trailer trailer :
                            trailers) {
                        try {
                            trailer.setMovie(movie);
                            trailer.save();

                        } catch (Exception ex){

                        }
                    }
        }
    }

    synchronized public static void syncReviews(Context context, final Movie movie, RestClient restClient) throws IOException {
        Response<ReviewResponse> reviewResponseCall = restClient.getApiService().loadReviews(String.valueOf(movie.getId()), RestClient.API_KEY).execute();
        if (reviewResponseCall.body() != null) {
            final List<Review> reviews = reviewResponseCall.body().getReviews();
            Log.d(LOG_TAG, "Number of reviews received: " + reviews.size());
            for (Review review :
                    reviews) {
                review.setMovie(movie);
                review.save();
            }
        }
    }
}
