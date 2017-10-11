package com.codepath.volunteerhero.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by jan_spidlen on 10/10/17.
 */

public class Event {

    public enum EventType {
        InPerson,
        Virtual
    }

    @NonNull
    public String id;

    @NonNull
    public String creatorId;

    @NonNull
    public String title;

    @NonNull
    public String description;

    // Main image of the event.
    @Nullable
    public String eventHeaderImageUrl;

    @NonNull
    public List<String> imageUrls;

    @NonNull
    public EventType type;

}
