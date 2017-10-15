package com.codepath.volunteerhero.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jan_spidlen on 10/10/17.
 */

public class Event {

    @NonNull
    public String id;

    // The organization of the event
    @NonNull
    public Carrier carrier;

    @NonNull
    public String title;

    @NonNull
    public double latitude;

    @NonNull
    public double longitude;

    @NonNull
    public String description;

    public String city;

    public String country;

    @NonNull
    public ArrayList<String> topics;

    // Main image of the event.
    @Nullable
    public String eventHeaderImageUrl;

    @NonNull
    public List<String> imageUrls;

    // Whether an event is in person or virtual
    @NonNull
    public boolean location_fixed;

    public String getLocation() {
        if (city == null || country == null) {
            return "Virtual";
        }
        return city + ", " + country;
    }

    public String getTopics() {
        StringBuilder result = new StringBuilder();
        for(String string : topics) {
            result.append(string);
            result.append(", ");
        }
        return result.length() > 0 ? result.substring(0, result.length() - 2): "";
    }

}
