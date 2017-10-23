package com.codepath.volunteerhero.data;

import com.codepath.volunteerhero.database.FirebaseDBHelper;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.settings.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jan_spidlen on 10/17/17.
 */

public class EventDataProvider extends DataProvider<Event> {

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
            mergedData.addAll(FirebaseDBHelper.getInstance().geFireBaseEvents());
            addOrUpdateData(mergedData);
            onFinish();
        });
    }
}
