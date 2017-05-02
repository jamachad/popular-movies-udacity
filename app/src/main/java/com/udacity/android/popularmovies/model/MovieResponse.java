package com.udacity.android.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jaime on 12-Mar-17.
 */

public class MovieResponse {
    private Integer page;

    @SerializedName("results")
    private List<Movie> movies = null;

    private Integer totalResults;
    private Integer totalPages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
