package com.codepath.volunteerhero.settings;

/**
 * @author tejalpar
 */
public class Filter {
    public static final String LOC_KEY = "LOCATION";
    public static final String SEARCH_QUERY = "SEARCH_QUERY";

    private String locationQuery;
    private String searchQuery;

    public String getLocationQuery() {
        return locationQuery;
    }

    public void setLocationQuery(String location) {
        this.locationQuery = location;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public boolean isFilterSet() {
        return (!(locationQuery == null || searchQuery == null));
    }
}
