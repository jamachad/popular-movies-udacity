package com.udacity.android.popularmovies.HelperClasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Date;

/**
 * Created by jamachad on 26/03/2016.
 */
public class Movie implements Parcelable {

    public int id;
    public String posterPath;
    public String originalTitle;
    public String overview;
    public Double voteAverage;
    public Date releaseDate;

    public Movie(int id, String posterPath, String originalTitle, String overview, Double voteAverage, Date releaseDate){
        this.id = id;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        posterPath = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
        long tempDate = in.readLong();
        releaseDate = tempDate == -1 ? null : new Date(tempDate);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(posterPath);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeLong(releaseDate != null ? releaseDate.getTime() : -1);
    }
}
