package com.codepath.volunteerhero.fragments;

import android.util.Log;

import com.codepath.volunteerhero.VolunteerHeroApplication;
import com.codepath.volunteerhero.database.FirebaseDBHelper;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.models.User;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dharinic on 10/22/17.
 */

public class UserSubscribedListFragment extends OpportunitiesListFragment {
    private static final String TAG = UserSubscribedListFragment.class.getSimpleName();

    public UserSubscribedListFragment() {
        //required empty
    }

    public static UserSubscribedListFragment newInstance(String param1, String param2) {
        UserSubscribedListFragment fragment = new UserSubscribedListFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args); */
        return fragment;
    }

    @Override
    public void startDataLoad() {
        Log.d(TAG, "Starting load of subscribed events");
        // get user subscribed events from firebase
        getSubscribedEvents();
        //mEventAdapter.addAll();
    }

    // Helper method to fetch all users & events
    private void getSubscribedEvents() {
        List<User> users = new ArrayList<>();
        List<Event>  subscribedEvents = new ArrayList<>();
        FirebaseDBHelper.getInstance().getUsersSubscribedEvents(VolunteerHeroApplication.getLoggedInUser(), new FirebaseDBHelper.DataChangeEventListener() {
            @Override
            public void onUserDataUpdated(User user) {
                User currentUser = VolunteerHeroApplication.getLoggedInUser();
                if (user.id.equals(currentUser.id)) {
                    if (user.events != null) {
                        subscribedEvents.addAll(user.events);
                        dataAdded(subscribedEvents);
                    }
                }
            }

            @Override
            public void onEventDataUpdated(Event event) {

            }

            @Override
            public void onUserInfoAvailable(User loggedInUser) {

            }

            @Override
            public void onUserInfoNotFound(FirebaseUser firebaseUser) {

            }
        });
    }

    @Override
    public void loadMoreData() {
        //pagination not available for firebase yet
    }
}
