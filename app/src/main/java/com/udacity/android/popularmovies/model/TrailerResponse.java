package com.udacity.android.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jaime on 12-Mar-17.
 */

public class TrailerResponse {
    private String id;
    @SerializedName("results")
    private List<Trailer> trailers = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
}
