package com.udacity.android.popularmovies.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jaime on 12-Mar-17.
 */

public class TrailerResponse {
    private Integer id;
    @SerializedName("results")
    private List<Trailer> trailers = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
}
