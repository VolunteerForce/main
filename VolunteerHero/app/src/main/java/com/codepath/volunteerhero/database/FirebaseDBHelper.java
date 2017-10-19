package com.codepath.volunteerhero.database;

import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.models.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by tejalpar
 */
public class FirebaseDBHelper {
    public static String TAG = FirebaseDBHelper.class.getSimpleName();

    private static final String USERS_NODE = "users";
    private static final String EVENTS_NODE = "events";
    private static final String CARRIER_NODE = "carrier";

    private static FirebaseDBHelper instance;
    private static FirebaseClient firebaseClient;

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

    public void addEvent(Event event) {
        DatabaseReference dfReference = firebaseClient.getFirebaseDatabase();

        event.id = dfReference.child(EVENTS_NODE).push().getKey();
        event.carrier.id = dfReference.child(EVENTS_NODE).child(CARRIER_NODE).push().getKey();
        dfReference.child(EVENTS_NODE).child(event.id).setValue(event);
    }

    public void updateEvent(Event event) {
        firebaseClient.getFirebaseDatabase().child(EVENTS_NODE).child(event.id).setValue(event);
    }
}
