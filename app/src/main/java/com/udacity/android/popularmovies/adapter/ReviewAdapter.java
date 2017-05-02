package com.udacity.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.android.popularmovies.R;
import com.udacity.android.popularmovies.model.Review;
import com.udacity.android.popularmovies.model.Trailer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jaime on 23-Apr-17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> mReviews;
    private Context mContext;

    public ReviewAdapter(List<Review> reviews, Context context) {
        this.mReviews = reviews;
        this.mContext = context;
    }


    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
        holder.textViewReviewAuthor.setText(mReviews.get(position).getAuthor());
        holder.textViewReviewFull.setText(mReviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        @BindView(R.id.textViewReviewAuthor)
        TextView textViewReviewAuthor;

        @BindView(R.id.textViewReviewFull) TextView textViewReviewFull;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
