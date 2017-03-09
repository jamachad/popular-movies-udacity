package com.udacity.android.popularmovies.data;

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
    @Column
    @PrimaryKey
    int id;

    @Column
    String name;

    @Column
    @Unique
    String posterPath;

    @Column
    String originalTitle;

    @Column
    String overview;

    @Column
    Double voteAverage;

    @Column
    Boolean favoriteMovie;

    @Column
    Date releaseDate;

    List<Review> reviews;

    List<Trailer> trailers;

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "reviews")
    public List<Review> getReviews() {
        if(reviews == null){
            reviews = new Select().from(Review.class).where(Condition.column(Review_Table.movie_id.getNameAlias()).eq(this.id)).queryList();
        }
        return reviews;
    }

    @OneToMany(methods = {OneToMany.Method.ALL}, variableName = "trailers")
    public List<Trailer> getTrailers(){
        if (trailers == null){
            //trailers = new Select().from(Trailer.class).where(Condition.column(Trailer_Table.))
        }
        return trailers;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }
}
