package com.codepath.volunteerhero.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by dharinic on 10/21/17.
 */

@Parcel(analyze={EventImage.class})
public class EventImage {
    public List<Link> links;
    public String description;

    public EventImage() {

    }
}
