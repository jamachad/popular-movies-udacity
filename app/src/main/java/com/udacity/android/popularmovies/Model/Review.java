package com.udacity.android.popularmovies.Model;

import com.google.gson.annotations.Expose;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by jamachad on 14/10/2016.
 */

@Table(database = Database.class)
//@Parcel(analyze = {Review.class})
public class Review extends BaseModel {

    @Expose
    @Column
    @PrimaryKey
    public String id;

    @Expose
    @Column
    public String author;

    @Expose
    @Column
    public String content;

    @Expose
    @Column
    public String url;

    @Column
    @ForeignKey(saveForeignKeyModel = false)
    Movie movie;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

