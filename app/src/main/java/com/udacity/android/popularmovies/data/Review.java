package com.udacity.android.popularmovies.data;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by jamachad on 14/10/2016.
 */

@Table(database = Database.class)
//@Parcel(analyze = {Review.class})
public class Review extends BaseModel {

    @Column
    @PrimaryKey
    int id;

    @Column
    String author;

    @Column
    String content;

    @Column
    @ForeignKey(saveForeignKeyModel = false)
    Movie movie;
}
