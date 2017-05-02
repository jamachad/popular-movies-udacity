package com.udacity.android.popularmovies.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.udacity.android.popularmovies.R;
import com.udacity.android.popularmovies.model.Trailer;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.udacity.android.popularmovies.BuildConfig.YOUTUBE_API_KEY;

/**
 * Created by jaime on 17-Apr-17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private List<Trailer> trailers;
    private Activity mActivity;

    public TrailerAdapter(List<Trailer> trailers, Activity callingActivity) {
        this.trailers = trailers;
        this.mActivity = callingActivity;
    }

    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trailer, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TrailerAdapter.ViewHolder holder, final int position) {
        holder.textViewTrailerNumber.setText("Trailer " + Integer.toString(position +1));

        holder.youTubeThumbnailView.initialize(YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(trailers.get(position).getKey());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                Toast.makeText(mActivity, mActivity.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        });

        holder.youTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.startActivity(YouTubeStandalonePlayer.createVideoIntent(mActivity,
                        YOUTUBE_API_KEY, trailers.get(position).getKey(), 0, true, true));
            }
        });
    }



    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewTrailerNumber) TextView textViewTrailerNumber;
        @BindView(R.id.youtube_thumbnail_more_trailers) YouTubeThumbnailView youTubeThumbnailView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
