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
//@Parcel(analyze = {Trailer.class})
public class Trailer extends BaseModel{

    @Expose
    @Column
    @PrimaryKey
    public int id;

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
    @ForeignKey(saveForeignKeyModel = false)
    Movie movie;
}


