package com.codepath.volunteerhero.data;

import android.os.Handler;
import android.util.Log;

import com.codepath.volunteerhero.models.BetterPlaceEventResponse;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.networking.BetterPlaceClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.facebook.GraphRequest.TAG;

/**
 * Created by jan_spidlen on 10/17/17.
 */

// TODO: make this class stateful to remember which page are we on.
public class BetterPlaceDataProvider {

    public interface DataLoadedCallback {
        void dataLoaded(List<Event> data);
    }

    public static BetterPlaceDataProvider getInstance() {
        return new BetterPlaceDataProvider();
    }

    String TAG = "jenda";
    private BetterPlaceClient client = BetterPlaceClient.getInstance();
    private Handler mHandler = new Handler();

    public void loadData(final int page, final DataLoadedCallback dataLoadedCallback) {

        client.getEvents(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to get a network response");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    String responseData = response.body().string();
                    JSONObject json = new JSONObject(responseData);
                    final BetterPlaceEventResponse betterPlaceEventResponse =
                            BetterPlaceEventResponse.parseJSON(responseData);
//                    Log.d(TAG, "b.eventList.size " + betterPlaceEventResponse.data.size() );
//
//                    dataLoadedCallback.dataLoaded(betterPlaceEventResponse.data);
                    Log.d(TAG, "responseData " + responseData);
                    Log.d(TAG, "id " +  betterPlaceEventResponse.data.get(0).id);
                    mHandler.post(() -> {
                        Log.d(TAG, "b.eventList.size " + betterPlaceEventResponse.data.size() );
                        dataLoadedCallback.dataLoaded(betterPlaceEventResponse.data);

//                        mEventAdapter.addAll(b.data);
                        Log.d(TAG, "successfully loaded dummy data for page " + page);
//                        srSwipeContainer.setRefreshing(false);
                        //update adapter
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, page);
    }
}
