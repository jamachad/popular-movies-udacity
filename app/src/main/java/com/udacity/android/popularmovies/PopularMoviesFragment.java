package com.udacity.android.popularmovies;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.udacity.android.popularmovies.model.Database;
import com.udacity.android.popularmovies.model.Movie;
import com.udacity.android.popularmovies.model.Movie_Table;
import com.udacity.android.popularmovies.sync.SyncIntentService;
import com.udacity.android.popularmovies.sync.SyncUtils;
import com.udacity.android.popularmovies.utilities.LoadedMoviesEvent;
import com.udacity.android.popularmovies.utilities.MoviesPreferences;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.parceler.Parcels;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, MovieAdapter.ListItemClickListener {

    MovieAdapter mMoviesAdapter;
    SharedPreferences mSharedPreferences;
    ProgressBar progressBar;
    RecyclerView mMoviesList;
    private int mPosition = RecyclerView.NO_POSITION;
    GridLayoutManager layoutManager;
    private static final String BUNDLE_RECYCLER_LAYOUT = "PopularMoviesFragment.recycler.layout";
    ProgressDialog progressDialog;

    public static final int ID_MOVIE_LOADER = 83;

    public static final String[] MOVIE_PROJECTION = {
            Movie_Table.posterPath.getNameAlias().toString()
    };

    public PopularMoviesFragment() {
        // Required empty public constructor
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadedMoviesEvent(LoadedMoviesEvent event){
       // Toast.makeText(getActivity(), "finished", Toast.LENGTH_SHORT).show();

        if(event.message == View.INVISIBLE)
                progressDialog.dismiss();


            //progressBar.setVisibility(View.INVISIBLE);
        else
            progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Downloading data for the first time", false,false);
            //progressBar.setVisibility(View.VISIBLE);



        getActivity().getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER,null,this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort_by_popular_movies:
                //Cursor
                MoviesPreferences.setMovieOrder(getActivity(), getString(R.string.pref_popular_order));
                getActivity().getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER,null,this);
                //Snackbar.make(getView(), R.string.no_internet, Snackbar.LENGTH_LONG).show();
                break;

            case R.id.sort_by_toprated_movies:
                MoviesPreferences.setMovieOrder(getActivity(), getString(R.string.pref_top_rated_order));
                getActivity().getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER,null,this);
                break;

            case R.id.sort_by_favorites:
                MoviesPreferences.setMovieOrder(getActivity(), getString(R.string.pref_favorites_order));
                getActivity().getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER,null,this);
                break;

            default:
                break;
        }
        //Snackbar.make(getView(), R.string.no_internet, Snackbar.LENGTH_LONG).show();


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mMoviesList.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, layoutManager.onSaveInstanceState());
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

        mMoviesList = (RecyclerView) rootView.findViewById(R.id.recycler_view_popular_movies_fragment);
        this.layoutManager = new GridLayoutManager(getActivity(),2);

        mMoviesList.setLayoutManager(layoutManager);
        mMoviesList.setHasFixedSize(true);
        mMoviesList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(3), true));
        mMoviesList.setItemAnimator(new DefaultItemAnimator());
        mMoviesAdapter = new MovieAdapter(getActivity(), this);
        mMoviesList.setAdapter(mMoviesAdapter);
        getActivity().getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
        if(savedInstanceState != null){

            //moviesList = savedInstanceState.getParcelableArrayList("movies");
            //mMoviesAdapter = new MovieAdapter(getActivity(),moviesList);
        }else{
            updateMovies();

            /*moviesList = new ArrayList<Movie>();
            mMoviesAdapter = new MovieAdapter(getActivity(),moviesList);
            mMoviesAdapter.clear();
            moviesList.clear();
            updateMovies();*/
        }


        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}



    @Override
    public void onStart() {
        super.onStart();
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void updateMovies() {
        SyncUtils.initialize(getActivity());
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
                /*mMoviesAdapter.clear();
                moviesList.clear();
                updateMovies();*/
                sharedPreferences.edit().putString(getString(R.string.option_changed), listType).commit();
            }else{
                Snackbar.make(getView(), R.string.no_internet, Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId){
            case ID_MOVIE_LOADER:
                String movieOrder = MoviesPreferences.getMovieOrder(getActivity());
                String orderBy = "";
                if(movieOrder.equals(getString(R.string.pref_popular_order))){
                    orderBy = Movie_Table.popularity.getNameAlias().getNameAsKey() + " DESC";
                }else if(movieOrder.equals(getString(R.string.pref_top_rated_order))){
                    orderBy = Movie_Table.voteAverage.getNameAlias().getNameAsKey() + " DESC";
                } else if(movieOrder.equals(getString(R.string.pref_favorites_order))){
                    orderBy = Movie_Table.originalTitle.getNameAlias().getNameAsKey() + " ASC";
                    return new CursorLoader(getActivity(),
                            Database.MovieProviderModel.CONTENT_URI,
                            null,
                            Movie_Table.favoriteMovie + " = 1",
                            null,
                            orderBy);
                }

                return new CursorLoader(getActivity(),
                        Database.MovieProviderModel.CONTENT_URI,
                        null,
                        Movie_Table.movie_order + " = ? OR " + Movie_Table.movie_order + " = ?",
                        new String[]{movieOrder, getActivity().getString(R.string.PopularAndTopRated_Movie)},
                        orderBy);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMoviesAdapter.swapCursor(data);
        if(mPosition==RecyclerView.NO_POSITION) mPosition = 0;
        mMoviesList.smoothScrollToPosition(mPosition);
        //if(data.getCount() != 0)
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(Movie clickedMovie) {
        Intent intentDetailMovie = new Intent(getActivity(), DetailActivity.class).putExtra("movie", Parcels.wrap(clickedMovie));
        startActivity(intentDetailMovie);
        Toast.makeText(getActivity(), "Movie clicked", Toast.LENGTH_SHORT).show();
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
