package com.codepath.volunteerhero.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by dharinic on 10/14/17.
 */
@IgnoreExtraProperties
@Parcel(analyze={Carrier.class})
public class Carrier extends BaseModelWithId implements Serializable {

    @NonNull
    public String name;

    @NonNull
    public String country;

    public Carrier() {
        //required default constructor
    }
}
