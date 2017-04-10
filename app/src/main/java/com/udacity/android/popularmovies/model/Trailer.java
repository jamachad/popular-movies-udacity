package com.udacity.android.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by jamachad on 14/10/2016.
 */

@Table(database = Database.class)
@Parcel(analyze = {Trailer.class})
public class Trailer extends BaseModel{

    @Expose
    @Column
    @PrimaryKey
    public String id;

    @Expose
    @Column
    public String name;

    @Expose
    @Column
    public String size;

    @Expose
    @Column
    public String key;


    @Column
    @ForeignKey(stubbedRelationship = true, saveForeignKeyModel = false)
    Movie movie;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }
}


