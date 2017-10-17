package com.codepath.volunteerhero.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dharinic on 10/14/17.
 */

public class BetterPlaceEventResponse {

    public List<Event> data;

    public BetterPlaceEventResponse() {
        data = new ArrayList<>();
    }

    public static BetterPlaceEventResponse parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        BetterPlaceEventResponse eventResponse = gson.fromJson(response,
                BetterPlaceEventResponse.class);
        return eventResponse;
    }
}
