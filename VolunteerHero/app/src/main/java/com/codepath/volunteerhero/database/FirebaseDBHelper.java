package com.codepath.volunteerhero.database;

import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.models.Subscription;
import com.codepath.volunteerhero.models.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tejalpar
 */
public class FirebaseDBHelper {
    public static String TAG = FirebaseDBHelper.class.getSimpleName();

    private static final String USERS_NODE = "users";
    private static final String EVENTS_NODE = "events";
    private static final String CARRIER_NODE = "carrier";
    private static final String SUBSCRIPTION_NODE = "subscription";

    private static FirebaseDBHelper instance;
    private static FirebaseClient firebaseClient;
    private static DataChangeEventListener dataChangeEventListener;

    public interface DataChangeEventListener {
        void onUserDataUpdated(User user);
    }

    public static FirebaseDBHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseDBHelper();
        }
        return instance;
    }

    private FirebaseDBHelper() {
        firebaseClient = FirebaseClient.getInstance();
    }

    public User saveUser(FirebaseUser user) {
        // add or update user
        User dbUser = new User(user.getUid(), user.getDisplayName(), user.getEmail(), user.getPhotoUrl().getPath());
        firebaseClient.getFirebaseDatabase().child(USERS_NODE).child(user.getUid()).setValue(dbUser);

        return dbUser;
    }

    public Event addEvent(Event event) {
        DatabaseReference dfReference = firebaseClient.getFirebaseDatabase();

        event.id = dfReference.child(EVENTS_NODE).push().getKey();
        event.carrier.id = dfReference.child(EVENTS_NODE).child(CARRIER_NODE).push().getKey();
        dfReference.child(EVENTS_NODE).child(event.id).setValue(event);
        return event;
    }

    public void updateEvent(Event event) {
        firebaseClient.getFirebaseDatabase().child(EVENTS_NODE).child(event.id).setValue(event);
    }

    public void addUsersSubscribedEvent(User user, Event subscribedEvent) {
        DatabaseReference dfReference = firebaseClient.getFirebaseDatabase();

        if (user.events == null) {
            List<Event> events = new ArrayList<>();
            events.add(subscribedEvent);
            user.events = events;
        } else {
            user.events.add(subscribedEvent);
        }
        dfReference.child(USERS_NODE).child(user.id).setValue(user);
    }

    public void getUsersSubcribedEvents(User lookupUser, DataChangeEventListener listener) {
        dataChangeEventListener = listener;
        firebaseClient.getFirebaseDatabase().child(USERS_NODE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if (dataChangeEventListener != null) {
                        dataChangeEventListener.onUserDataUpdated(user);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addUsersSubscribedEvents(User user, List<Event> subscribedEvents) {
        DatabaseReference dfReference = firebaseClient.getFirebaseDatabase();

        Subscription subscription = new Subscription();
        subscription.id = dfReference.child(SUBSCRIPTION_NODE).push().getKey();
        subscription.date = System.currentTimeMillis();
        subscription.user = user;
        subscription.events = subscribedEvents;
        dfReference.child(SUBSCRIPTION_NODE).child(subscription.id).setValue(subscription);
    }

    public void updateUsersSubscribedEvents(Subscription subscription) {
        firebaseClient.getFirebaseDatabase().child(SUBSCRIPTION_NODE).child(subscription.id).setValue(subscription);
    }
}
