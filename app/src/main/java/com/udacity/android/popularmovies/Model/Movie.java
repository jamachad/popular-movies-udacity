package com.udacity.android.popularmovies.Model;

import com.google.gson.annotations.Expose;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.sql.Date;
import java.util.List;

/**
 * Created by jamachad on 06/10/2016.
 */

@Table(database = Database.class)
//@Parcel(analyze = {Movie.class})
public class Movie extends BaseModel {

    @Expose
    @Column
    private String posterPath;

    @Expose
    @Column
    public Boolean adult;

    @Expose
    @Column
    public String overview;

    @Expose
    @Column
    public String releaseDate;

    @Expose
    @Column
    @PrimaryKey
    private Integer id;

    @Expose
    @Column
    public String originalTitle;

    @Expose
    @Column
    public String originalLanguage;

    @Expose
    @Column
    public String title;

    @Expose
    @Column
    public String backdropPath;

    @Expose
    @Column
    public Double popularity;

    @Expose
    @Column
    public Integer voteCount;

    @Expose
    @Column
    public Boolean video;

    @Expose
    @Column
    public Double voteAverage;

    @Expose
    @Column
    public Boolean favoriteMovie;

    List<Review> reviews;

    List<Trailer> trailers;

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "reviews")
    public List<Review> getReviews() {
        if(reviews == null){
            reviews = new Select().from(Review.class).where(Condition.column(Review_Table.movie_id.getNameAlias())
                    .eq(this.id)).queryList();
        }
        return reviews;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "trailers")
    public List<Trailer> getTrailers(){
        if (trailers == null){
            trailers = new Select().from(Trailer.class).where(Condition.column(Trailer_Table.movie_id.getNameAlias())
                    .eq(this.id)).queryList();
        }
        return trailers;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Boolean getFavoriteMovie() {
        return favoriteMovie;
    }

    public void setFavoriteMovie(Boolean favoriteMovie) {
        this.favoriteMovie = favoriteMovie;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
}
