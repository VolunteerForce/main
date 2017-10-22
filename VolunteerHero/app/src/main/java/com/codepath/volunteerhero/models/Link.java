package com.codepath.volunteerhero.models;

import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

/**
 * Created by dharinic on 10/21/17.
 */
@IgnoreExtraProperties
@Parcel(analyze={Link.class})
public class Link {
    public String rel;
    public String href;

    public Link() {

    }
}
