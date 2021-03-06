package com.udacity.android.popularmovies;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.EventLog;
import android.util.Log;
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
import com.udacity.android.popularmovies.utilities.FirstTimeLoad;
import com.udacity.android.popularmovies.utilities.InternetChangeEvent;
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
    Parcelable mSavedRecyclerLayoutState;
    public static Boolean fabPressed = false;
    private NetworkReceiver mNetworkReceiver;

    public static void setFabPressed(boolean wasFabPressed){
        fabPressed = wasFabPressed;
    }

    private Callback listener;


    public static final int ID_MOVIE_LOADER = 83;

    public static final String[] MOVIE_PROJECTION = {
            Movie_Table.posterPath.getNameAlias().toString()
    };

    public PopularMoviesFragment() {
        // Required empty public constructor
    }

    public interface Callback{
        public  void onMovieItemSelected(Movie movieClicked);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Callback)
            listener = (Callback) context;
        else
            throw new ClassCastException(context.toString() + " must implement PopularMoviesFragment.OnItemSelectedListener.");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadedMoviesEvent(LoadedMoviesEvent event){
        if(event.message == View.INVISIBLE)
                progressDialog.dismiss();
        else
            progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Downloading data for the first time", false,false);

        getActivity().getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER,null,this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onInternetChangeEvent(InternetChangeEvent event){
        if(event.message == true){
            updateMovies();
        }else{
            Snackbar.make(getView(), R.string.no_internet, Snackbar.LENGTH_LONG).show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort_by_popular_movies:
                MoviesPreferences.setMovieOrder(getActivity(), getString(R.string.pref_popular_order));
                if(getResources().getBoolean(R.bool.twoPaneMode)) mSavedRecyclerLayoutState = null;
                getActivity().getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER,null,this);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.pref_popular_label_order));
                mMoviesList.smoothScrollToPosition(0);

                break;

            case R.id.sort_by_toprated_movies:
                if(getResources().getBoolean(R.bool.twoPaneMode)) mSavedRecyclerLayoutState = null;
                MoviesPreferences.setMovieOrder(getActivity(), getString(R.string.pref_top_rated_order));
                getActivity().getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER,null,this);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.pref_toprated_label_order));
                mMoviesList.smoothScrollToPosition(0);
                break;

            case R.id.sort_by_favorites:
                MoviesPreferences.setMovieOrder(getActivity(), getString(R.string.pref_favorites_order));
                if(getResources().getBoolean(R.bool.twoPaneMode)) mSavedRecyclerLayoutState = null;
                getActivity().getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER,null,this);
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.pref_favorites_order_label));
                mMoviesList.smoothScrollToPosition(0);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
        {
          //  int state = savedInstanceState.getInt("position");
            mSavedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            //mMoviesList.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
           // mMoviesList.smoothScrollToPosition(state);
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, ((GridLayoutManager) mMoviesList.getLayoutManager()).onSaveInstanceState());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            mNetworkReceiver = new NetworkReceiver();
            getActivity().registerReceiver(mNetworkReceiver, intentFilter);
        }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected() && activeNetwork.isAvailable()) {
                updateMovies();
            } else {
                Snackbar.make(getView(), R.string.no_internet, Snackbar.LENGTH_LONG).show();
            }
        }

        setTitle();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar_main);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String listType = mSharedPreferences.getString(getString(R.string.pref_listType_key), getString(R.string.pref_popular_order));
        getActivity().getPreferences(Context.MODE_PRIVATE)
                .edit().putString(getString(R.string.option_changed), listType).commit();

        mMoviesList = (RecyclerView) rootView.findViewById(R.id.recycler_view_popular_movies_fragment);
        this.layoutManager = new GridLayoutManager(getActivity(),2);
        this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position % 3 == 0 || position % 3 == 1) {
                  //  Log.e("TAG", "Position: " + position +" position % 3= " + position % 3 + " return 1");
                    return 1;
                } else {
                  //  Log.e("TAG", "Position: " + position +" position % 3= " + position % 3 + " return 2");
                    return 2;
                }
            }
        });

        mMoviesList.setLayoutManager(layoutManager);
        mMoviesList.setHasFixedSize(true);
        mMoviesList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(3), true));
        mMoviesList.setItemAnimator(new DefaultItemAnimator());
        mMoviesAdapter = new MovieAdapter(getActivity(), this);
        mMoviesList.setAdapter(mMoviesAdapter);
        getActivity().getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);


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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                getActivity().unregisterReceiver(mNetworkReceiver);
            }catch (IllegalArgumentException ex){
                ex.printStackTrace();
            }
        }
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void setTitle(){
        if (MoviesPreferences.getMovieOrder(getActivity()).equals(getString(R.string.pref_popular_order)))
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.pref_popular_label_order));

        else if(MoviesPreferences.getMovieOrder(getActivity()).equals(getString(R.string.pref_top_rated_order)))
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.pref_toprated_label_order));

        else if(MoviesPreferences.getMovieOrder(getActivity()).equals(getString(R.string.pref_favorites_order)))
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.pref_favorites_order_label));
   }

    private void updateMovies() {
        SyncUtils.initialize(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
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
        final Cursor defaultRecordToLoad = data;
        mMoviesAdapter.swapCursor(data);
        if(mPosition==RecyclerView.NO_POSITION) mPosition = 0;
        if(mSavedRecyclerLayoutState != null && getResources().getBoolean(R.bool.twoPaneMode) == false){
            mMoviesList.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);

        }else{
           // mMoviesList.smoothScrollToPosition(mPosition);
        }
        if(mPosition == 0 && mSavedRecyclerLayoutState == null && fabPressed == false && data.getCount() > 0) {
            new Handler().post(new Runnable() {
                public void run() {
                    Movie firstMovie = Movie.populateMovie(defaultRecordToLoad, 0);
                    EventBus.getDefault().post(new FirstTimeLoad(firstMovie));
                }
            });
        }
        setFabPressed(false);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMoviesAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(Movie clickedMovie) {
        listener.onMovieItemSelected(clickedMovie);
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
