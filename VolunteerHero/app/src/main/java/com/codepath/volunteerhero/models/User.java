package com.codepath.volunteerhero.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

/**
 * Created by jan_spidlen on 10/10/17.
 */

public class User {

    @NonNull
    public String id;

    @NonNull
    public String email;

    @NonNull
    public Name name;

    @Nullable
    public String description;

    @Nullable
    public String profileImageUrl;
}
