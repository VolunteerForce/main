package com.codepath.volunteerhero.data;

import com.codepath.volunteerhero.database.FirebaseDBHelper;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.models.User;
import com.codepath.volunteerhero.settings.Filter;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jan_spidlen on 10/17/17.
 */

public class EventDataProvider extends DataProvider<Event> implements FirebaseDBHelper.DataChangeEventListener {

    private static EventDataProvider INSTANCE;

    public static  EventDataProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventDataProvider();
        }
        return INSTANCE;
    }

    @Override
    protected void loadDataInternal(Filter filter) {
        BetterPlaceDataProvider.getInstance().loadData(state.page, filter, data -> {
            List<Event> mergedData = new ArrayList<>();
            mergedData.addAll(data);
            addOrUpdateData(mergedData);
            onFinish();
        });
        FirebaseDBHelper.getInstance().getEventList(this);
    }

    @Override
    public void onUserDataUpdated(User user) {

    }

    @Override
    public void onEventDataUpdated(Event event) {
        List<Event> events = new ArrayList<>();
        events.add(event);
        addOrUpdateData(events);
    }

    @Override
    public void onUserInfoAvailable(User loggedInUser) {

    }

    @Override
    public void onUserInfoNotFound(FirebaseUser firebaseUser) {

    }
}
