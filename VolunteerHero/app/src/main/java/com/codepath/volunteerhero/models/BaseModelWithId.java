package com.codepath.volunteerhero.models;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by jan_spidlen on 10/17/17.
 */

public abstract class BaseModelWithId implements Serializable {

    @NonNull
    public String id;
}
