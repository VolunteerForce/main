package com.codepath.volunteerhero.models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by jan_spidlen on 10/10/17.
 */
@IgnoreExtraProperties
@Parcel(analyze={Event.class})
public class Event extends BaseModelWithId implements Serializable{

    // The organization of the event
    @NonNull
    public Carrier carrier;

    public User creator;

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

    @NonNull
    public Contact contact;

    public EventImage image;

    public int vacancies;

    public ArrayList<String> activities;

    public String getLocation() {
        if (city == null || country == null) {
            return "Virtual";
        }
        return city + ", " + country;
    }

    public String getTopics() {
        if (topics == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for(String string : topics) {
            result.append(string);
            result.append(", ");
        }
        return result.length() > 0 ? result.substring(0, result.length() - 2): "";
    }

    public String getActivities() {
        if (activities == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for(String string : activities) {
            result.append(string);
            result.append(", ");
        }
        return result.length() > 0 ? result.substring(0, result.length() - 2): "";
    }

    public Event updateFromPlace(Place place, Context context) throws IOException {
        this.latitude = place.getLatLng().latitude;
        this.longitude = place.getLatLng().longitude;
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = gcd.getFromLocation(this.latitude, this.longitude, 1);
        Log.d("jenda", "addresses: " + addresses);
        for (Address address: addresses) {
            if (address.getCountryName() != null) {
                this.country = address.getCountryName();
                this.city = address.getLocality();
                break;
            }
        }
        return this;
    }

    public String getImageUrl() {
        if (eventHeaderImageUrl != null) {
            return eventHeaderImageUrl;
        }
        if (image == null || image.links == null) {
            return  null;
        }
        List<Link> imageLinks = image.links;
        if (!image.links.isEmpty()) {
            return imageLinks.get(imageLinks.size() -1).href;
        }
        return null;
    }

    public Event() {
        //required default constructor
    }
}
