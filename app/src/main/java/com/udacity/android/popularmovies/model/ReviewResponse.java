package com.udacity.android.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jaime on 12-Mar-17.
 */

public class ReviewResponse {


    private Integer id;
    private Integer page;

    @SerializedName("results")
    private List<Review> reviews = null;

    private Integer totalPages;

    private Integer totalResults;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
