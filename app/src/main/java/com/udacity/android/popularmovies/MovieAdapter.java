package com.udacity.android.popularmovies;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import com.udacity.android.popularmovies.HelperClasses.PaletteTransformation;
import com.udacity.android.popularmovies.model.Database;
import com.udacity.android.popularmovies.model.Movie;
import com.udacity.android.popularmovies.model.Movie_Table;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

import static android.view.FrameMetrics.ANIMATION_DURATION;

/**
 * Created by jaime on 21-Mar-17.
 */

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    private static final String TAG = MovieAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;

    private Cursor mCursor;
    private final Context mContext;
    private static int viewHolderCount;
    private int mNumberItems;

    public interface ListItemClickListener{
        void onListItemClick(Movie clickedMovie);
    }

    public MovieAdapter(Context context, ListItemClickListener listener) {
        mContext = context;
        mOnClickListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.grid_item_popular_movies;
        boolean shouldAttachToParentImmediately = false;

        View view  = LayoutInflater.from(mContext).inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        //view.setFocusable(true);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Log.d(TAG, "#"+position);
        mCursor.moveToPosition(position);
        String imgUrl = "http://image.tmdb.org/t/p/w780"+mCursor.getString(mCursor.getColumnIndex(Movie_Table.posterPath.getNameAlias().getNameAsKey()));
        String movieTitle = mCursor.getString(mCursor.getColumnIndex(Movie_Table.title.getNameAlias().getNameAsKey()));
        Double voteAverage = mCursor.getDouble(mCursor.getColumnIndex(Movie_Table.voteAverage.getNameAlias().getNameAsKey()));
        //setRelativeLayoutBackground(holder.movieRelativeLayout);
        //Picasso.with(mContext).load("http://image.tmdb.org/t/p/w780"+imgUrl).into(holder.moviePosterImageView);
        holder.movieViewCountTextView.setText(mContext.getString(R.string.Rating) + voteAverage.toString());
        holder.movieTitleTextView.setText(movieTitle);
        loadImage(holder, imgUrl);
    }

    public void loadImage(final MovieViewHolder viewHolder, String imgUrl){
        Picasso.with(mContext)
                .load(imgUrl)
                .transform(PaletteTransformation.instance())
                .into(viewHolder.moviePosterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) viewHolder.moviePosterImageView.getDrawable()).getBitmap();
                        if (bitmap != null) {
                            Palette palette = Palette.from(bitmap).generate();
                            Palette.Swatch swatchVibrant = palette.getVibrantSwatch();
                            Palette.Swatch swatchMuted = palette.getVibrantSwatch();
                            Palette.Swatch swatchDominant = palette.getDominantSwatch();
                            if (swatchDominant != null) {
                                viewHolder.movieRelativeLayout.setBackgroundColor(swatchDominant.getRgb());
                                viewHolder.movieTitleTextView.setTextColor(swatchDominant.getTitleTextColor());
                                viewHolder.movieViewCountTextView.setTextColor(swatchDominant.getBodyTextColor());
                            } else if (swatchDominant != null) {
                                viewHolder.movieRelativeLayout.setBackgroundColor(swatchDominant.getRgb());
                                viewHolder.movieTitleTextView.setTextColor(swatchDominant.getTitleTextColor());
                                viewHolder.movieViewCountTextView.setTextColor(swatchDominant.getBodyTextColor());
                            }
                        }
                    }

                    @Override
                    public void onError() {
                        Log.e(TAG, "error");
                    }
                });
    }

    private void animateCard(MovieAdapter.MovieViewHolder holder, ColorStateList previousColor, int nextColor){
        ObjectAnimator animator = ObjectAnimator.ofInt(holder.movieCardView, "cardBackgroundColor", previousColor.getDefaultColor(), nextColor).setDuration(ANIMATION_DURATION);
        animator.setEvaluator(new ArgbEvaluator());
        animator.start();

        holder.movieTitleTextView.setTextColor(nextColor);
        holder.movieViewCountTextView.setTextColor(nextColor);
    }

    @Override
    public int getItemCount() {
        if(mCursor == null) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        if (mCursor != null) mCursor.setNotificationUri(mContext.getContentResolver(),Database.MovieProviderModel.CONTENT_URI);
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
         ImageView moviePosterImageView;
         TextView movieTitleTextView;
         TextView movieViewCountTextView;
         RelativeLayout movieRelativeLayout;
        CardView movieCardView;
        public MovieViewHolder(View itemView) {
            super(itemView);
            moviePosterImageView = (ImageView) itemView.findViewById(R.id.moviePoster);
            movieTitleTextView = (TextView) itemView.findViewById(R.id.movie_title);
            movieViewCountTextView = (TextView) itemView.findViewById(R.id.movie_vote_count);
            movieRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.grid_item_relative_layout);
            movieCardView = (CardView) itemView.findViewById(R.id.card_view_popular_movies);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(Movie.populateMovie(mCursor, clickedPosition));
        }
    }
}
