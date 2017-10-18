package com.codepath.volunteerhero.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by jan_spidlen on 10/10/17.
 */
@IgnoreExtraProperties
public class Subscription extends BaseModelWithId {

    @NonNull
    public Date date;

    @NonNull
    public String userId;

    @NonNull
    public String eventId;

    public Subscription() {
        //required default constructor
    }
}
