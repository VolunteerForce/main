package com.codepath.volunteerhero.networking;

import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by dharinic on 10/14/17.
 */

public class BetterPlaceClient {

    public static final String TAG = BetterPlaceClient.class.getSimpleName();
    private String BETTER_PLACE_URL = "https://api.betterplace.org/en/api_v4/volunteering.json";

    private static BetterPlaceClient sInstance;
    private final OkHttpClient mClient;

    private BetterPlaceClient() {
        mClient = new OkHttpClient();
    }

    public static BetterPlaceClient getInstance() {
        if (sInstance == null) {
            sInstance = new BetterPlaceClient();
        }
        return sInstance;
    }

    public void getEvents(Callback callback, int page) {
        getEvents(callback, page, null, null);
    }

    public void getEvents(Callback callback, int page, String location, String searchQ) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BETTER_PLACE_URL).newBuilder();
        if (location != null) {
            urlBuilder.addQueryParameter("around", location);
        }
        if (searchQ != null) {
            urlBuilder.addQueryParameter("q", searchQ);
        }
        urlBuilder.addQueryParameter("page", String.valueOf(page));

        getEventsInternal(callback, urlBuilder);
    }

    public void getEventsInternal(Callback callback, HttpUrl.Builder urlBuilder){

        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        mClient.newCall(request).enqueue(callback);
    }
}
