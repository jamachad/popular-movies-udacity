package com.udacity.android.popularmovies;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.squareup.picasso.Picasso;
import com.udacity.android.popularmovies.adapter.TrailerAdapter;
import com.udacity.android.popularmovies.adapter.ReviewAdapter;
import com.udacity.android.popularmovies.databinding.MovieDetailFragmentBinding;
import com.udacity.android.popularmovies.model.Database;
import com.udacity.android.popularmovies.model.Movie;
import com.udacity.android.popularmovies.model.Movie_Table;

import org.parceler.Parcels;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment{
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Movie mMovie;
    private static final String LOG_DETAIL_FRAGMENT = DetailActivityFragment.class.getSimpleName();
    private static final String BUNDLE_RECYCLER_TRAILERS_LAYOUT = "DetailActivityFragment.recycler.trailers.layout";

    private ShareActionProvider mShareActionProvider;

    MovieDetailFragmentBinding mBinding;

    public DetailActivityFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(BUNDLE_RECYCLER_TRAILERS_LAYOUT, mBinding.recyclerViewMoreTrailers.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            getActivity().finish();
            return true;
        }else if(item.getItemId() == R.id.sort_by_popular_movies){
            startActivity(new Intent(getActivity(),SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.detail, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + mMovie.getTrailers().get(0).getKey());
        setShareIntent(shareIntent);
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.movie_detail_fragment, container, false);
        View rootView = mBinding.getRoot();
        setHasOptionsMenu(true);

        mMovie = Parcels.unwrap(getActivity().getIntent().getExtras().getParcelable("movie"));
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle(mMovie.title);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ImageView posterImageView = mBinding.posterDetail;
        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w780"+mMovie.getPosterPath()).into(posterImageView);
        Log.d(LOG_DETAIL_FRAGMENT, "http://image.tmdb.org/t/p/w780"+mMovie.getPosterPath());

        mBinding.textViewMovieTitle.setText(mMovie.getTitle());
        mBinding.descriptionDetail.setText(mMovie.overview);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mMovie.getTitle());
        mBinding.userRatingDetail.setText(mMovie.voteAverage.toString());
        mBinding.releaseDate.setText(mMovie.getReleaseDate());

        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w1280"+mMovie.getBackdropPath()).into(mBinding.movieCover);
        Log.d(LOG_DETAIL_FRAGMENT, mMovie.getBackdropPath() );

        if(mMovie.getTrailers().size() > 0){
            TrailerAdapter trailerAdapter = new TrailerAdapter(mMovie.getTrailers(), getActivity());
            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            mBinding.recyclerViewMoreTrailers.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
            mBinding.recyclerViewMoreTrailers.setLayoutManager(horizontalLayoutManager);
            mBinding.recyclerViewMoreTrailers.setAdapter(trailerAdapter);
        }else{
            mBinding.textViewNoAvailableTrailers.setVisibility(View.VISIBLE);
        }

        if(mMovie.getReviews().size() > 0){
            ReviewAdapter reviewAdapter = new ReviewAdapter(mMovie.getReviews(), getActivity());
            LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mBinding.recyclerViewReviews.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
            mBinding.recyclerViewReviews.setLayoutManager(reviewLayoutManager);
            mBinding.recyclerViewReviews.setAdapter(reviewAdapter);
        }else{
            mBinding.textViewNoAvailableReviews.setVisibility(View.VISIBLE);
        }


        //final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);

        if(mMovie.getFavoriteMovie() == 0) mBinding.floatingActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        mBinding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMovie.getFavoriteMovie() == 0){
                    mMovie.setFavoriteMovie(1);
                    mBinding.floatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                }else if(mMovie.getFavoriteMovie() == 1){
                    mMovie.setFavoriteMovie(0);
                    mBinding.floatingActionButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }
                SQLite.update(Movie.class)
                        .set(Movie_Table.favoriteMovie.eq(mMovie.getFavoriteMovie()))
                        .where(Movie_Table.id.is(mMovie.getId()))
                        .execute();
                getActivity().getContentResolver().notifyChange(Database.MovieProviderModel.CONTENT_URI,  null);
            }
        });

        if(savedInstanceState != null){
            try {
                Parcelable savedRecyclerTrailersLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_TRAILERS_LAYOUT);
                mBinding.recyclerViewMoreTrailers.getLayoutManager().onRestoreInstanceState(savedRecyclerTrailersLayoutState);

            }catch (Exception ex){
                Log.e(LOG_DETAIL_FRAGMENT, ex.toString());
            }
        }

        return rootView;
    }
}
