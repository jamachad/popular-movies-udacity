package com.udacity.android.popularmovies.data;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * Created by jamachad on 14/10/2016.
 */

@Table(database = Database.class)
//@Parcel(analyze = {Trailer.class})
public class Trailer {
    @Column
    @PrimaryKey
    int id;

    @Column
    String name;

    @Column
    String size;

    @Column
    String key;
}
