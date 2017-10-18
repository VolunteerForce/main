package com.codepath.volunteerhero.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by jan_spidlen on 10/10/17.
 */
@IgnoreExtraProperties
public class User {

    @NonNull
    public String id;

    @NonNull
    public String email;

    @NonNull
    public String name;

    @Nullable
    public String description;

    @Nullable
    public String profileImageUrl;

    public User() {
        //required default constructor
    }

    public User(@NonNull String uuid, @NonNull String name, @NonNull String email, @Nullable String photoUrl) {
        this.id = uuid;
        this.name = name;
        this.email = email;
        this.profileImageUrl = photoUrl;
    }
}
