package com.codepath.volunteerhero.settings;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tejalpar on 10/22/17.
 */
public class FilterSettings {
    public static final String SHARED_PREF_FILE = "FilterPreference";
    public static final String LOC_KEY = "LOCATION";
    public static final String SEARCH_QUERY = "SEARCH_QUERY";

    private static FilterSettings instance;
    private SharedPreferences sharedPreferences;

    public static FilterSettings getInstance(Context context) {
        if (instance == null) {
            instance = new FilterSettings(context);
        }
        return instance;
    }

    private FilterSettings(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_FILE,Context.MODE_PRIVATE);
    }

    public void saveFilter(Filter filter) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putString(LOC_KEY, filter.getLocationQuery());
        prefsEditor.putString(SEARCH_QUERY, filter.getSearchQuery());
        prefsEditor.apply();
    }

    public Filter retrieveFilter() {
        Filter filter = new Filter();
        if (sharedPreferences != null) {
            filter.setLocationQuery(sharedPreferences.getString(LOC_KEY, ""));
            filter.setSearchQuery(sharedPreferences.getString(SEARCH_QUERY, ""));
        }
        return filter;
    }
}
