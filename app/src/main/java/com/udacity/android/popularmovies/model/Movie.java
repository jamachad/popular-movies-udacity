package com.udacity.android.popularmovies.model;

import android.database.Cursor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by jamachad on 06/10/2016.
 */

@Table(database = Database.class, insertConflict = ConflictAction.IGNORE)
@Parcel(analyze = {Movie.class})
public class Movie extends BaseModel {

    public Movie() {}

    @Expose
    @Column
    @SerializedName("poster_path")
    public String posterPath;

    @Expose
    @Column
    public Boolean adult;

    @Expose
    @Column
    public String overview;

    @Expose
    @Column
    @SerializedName("release_date")
    public String releaseDate;

    @Expose
    @Column
    @PrimaryKey
    public Integer id;

    @Expose
    @Column
    @SerializedName("original_title")
    public String originalTitle;

    @Expose
    @Column
    @SerializedName("original_language")
    public String originalLanguage;

    @Expose
    @Column
    public String title;

    @Expose
    @Column
    @SerializedName("backdrop_path")
    public String backdropPath;

    @Expose
    @Column
    public Double popularity;

    @Expose
    @Column
    @SerializedName("vote_count")
    public Integer voteCount;

    @Expose
    @Column
    public Boolean video;

    @Expose
    @Column
    @SerializedName("vote_average")
    public Double voteAverage;

    @Expose
    @Column
    @SerializedName("favorite_movie")
    public int favoriteMovie;

    /*
    - Popular
    - Top_rated
     */
    @Expose
    @Column
    public String movie_order;


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



    public static Movie populateMovie(Cursor cursor, int index){
        cursor.moveToPosition(index);
        String imgUrl = cursor.getString(cursor.getColumnIndex(Movie_Table.posterPath.getNameAlias().getNameAsKey()));
        String movieTitle = cursor.getString(cursor.getColumnIndex(Movie_Table.title.getNameAlias().getNameAsKey()));
        Double voteAverage = cursor.getDouble(cursor.getColumnIndex(Movie_Table.voteAverage.getNameAlias().getNameAsKey()));
        String overView = cursor.getString(cursor.getColumnIndex(Movie_Table.overview.getNameAlias().getNameAsKey()));
        String releaseDate = cursor.getString(cursor.getColumnIndex(Movie_Table.releaseDate.getNameAlias().getNameAsKey()));
        int id = cursor.getInt(cursor.getColumnIndex(Movie_Table.id.getNameAlias().getNameAsKey()));
        int isFavorite = cursor.getInt(cursor.getColumnIndex(Movie_Table.favoriteMovie.getNameAlias().getNameAsKey()));
        return new Movie(imgUrl, overView, releaseDate, id, movieTitle, voteAverage, isFavorite);
    }

    public Movie(String posterPath, String overview, String releaseDate, Integer id, String title, Double voteAverage, int favoriteMovie) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.title = title;
        this.voteAverage = voteAverage;
        this.favoriteMovie = favoriteMovie;
    }


    public String getMovie_order() {
        return movie_order;
    }

    public void setMovie_order(String movie_type) {
        this.movie_order = movie_order;
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

    public int getFavoriteMovie() {
        return favoriteMovie;
    }

    public void setFavoriteMovie(int favoriteMovie) {
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
