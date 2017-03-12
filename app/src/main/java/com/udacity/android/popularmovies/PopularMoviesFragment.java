package com.udacity.android.popularmovies;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.udacity.android.popularmovies.HelperClasses.Movie;
import com.udacity.android.popularmovies.HelperClasses.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularMoviesFragment extends Fragment {

    MovieAdapter mMoviesAdapter;
    ArrayList<Movie> moviesList;
    SharedPreferences mSharedPreferences;
    ProgressBar progressBar;

    public PopularMoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_settings){
            startActivity(new Intent(getActivity(),SettingsActivity.class));
            return true;
        }if(item.getItemId() == R.id.action_refresh){
            MyApplication myApplication = (MyApplication) getActivity().getApplicationContext();
            if(myApplication.isInternetAvailable()==true){
                mMoviesAdapter.clear();
                moviesList.clear();
                updateMovies();

            }else{
                Snackbar.make(getView(), R.string.no_internet, Snackbar.LENGTH_LONG)
                        .show();
            }
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.popular_movies_fragment, menu);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movies", moviesList);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar_main);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String listType = mSharedPreferences.getString(getString(R.string.pref_listType_key), getString(R.string.pref_popular_order));
        getActivity().getPreferences(Context.MODE_PRIVATE)
                .edit().putString(getString(R.string.option_changed), listType).commit();


        if(savedInstanceState != null){
            moviesList = savedInstanceState.getParcelableArrayList("movies");
            mMoviesAdapter = new MovieAdapter(getActivity(),moviesList);
        }else{
            moviesList = new ArrayList<Movie>();
            mMoviesAdapter = new MovieAdapter(getActivity(),moviesList);
            mMoviesAdapter.clear();
            moviesList.clear();
            updateMovies();
        }

        final GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(mMoviesAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movieItem = mMoviesAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra("movie", movieItem);
                startActivity(intent);
            }
        });


        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
    }

    private void updateMovies() {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String listType = sharedPreferences.getString(getString(R.string.pref_listType_key), getString(R.string.pref_popular_order));
        fetchMoviesTask.execute(listType);
    }

    public void getMoviesDataFromJSON(String moviesJsonStr) throws JSONException {
        String PopularMoviesList = "results";
        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray jsonArray = moviesJson.getJSONArray(PopularMoviesList);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonMovie = jsonArray.getJSONObject(i);
            Log.i("movieName",jsonMovie.getString("original_title"));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Movie movie = new Movie(jsonMovie.getInt("id"),jsonMovie.getString("poster_path"),jsonMovie.getString("original_title"),jsonMovie.getString("overview"),jsonMovie.getDouble("vote_average"),
                        new Date((simpleDateFormat.parse(jsonMovie.getString("release_date"))).getTime()));
                moviesList.add(movie);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        Log.d("JSON",jsonArray.toString()) ;
        Log.d("MAdapter",String.valueOf(mMoviesAdapter.getCount()));
    }

    public class FetchMoviesTask extends AsyncTask<String,Void,String[]>{
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            try {
                final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/movie/"+params[0]+"?";
                final String APPID_PARAM = "api_key";

                // Construct the URL for the MovieDB query
                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon().appendQueryParameter(APPID_PARAM,BuildConfig.THE_MOVIE_DB_API_KEY).build();

                URL url = new URL(builtUri.toString());
Log.i(LOG_TAG, "URL = " + url.toString());
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
                try {
                    getMoviesDataFromJSON(moviesJsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(LOG_TAG,moviesJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the movies data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            progressBar.setVisibility(View.INVISIBLE);
            mMoviesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String listType = sharedPreferences.getString(getString(R.string.pref_listType_key), getString(R.string.pref_popular_order));
        String oldListType = sharedPreferences.getString(getString(R.string.option_changed), getString(R.string.pref_popular_order));
        if(listType != oldListType){
            MyApplication myApplication = (MyApplication) getActivity().getApplicationContext();
            if(myApplication.isInternetAvailable()==true){
                mMoviesAdapter.clear();
                moviesList.clear();
                updateMovies();
                sharedPreferences.edit().putString(getString(R.string.option_changed), listType).commit();
            }else{
                Snackbar.make(getView(), R.string.no_internet, Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }
}
