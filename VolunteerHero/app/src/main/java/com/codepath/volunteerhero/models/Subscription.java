package com.codepath.volunteerhero.models;

import android.support.annotation.NonNull;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by jan_spidlen on 10/10/17.
 */
@IgnoreExtraProperties
public class Subscription extends BaseModelWithId {

    @NonNull
    public long date;

    @NonNull
    public User user;

    @NonNull
    public List<Event> events;

    public Subscription() {
        //required default constructor
    }
}
