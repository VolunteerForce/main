package com.codepath.volunteerhero.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by tejalpar
 */
public class FirebaseClient {

    private static FirebaseClient instance;
    private DatabaseReference databaseReference;

    public static FirebaseClient getInstance() {
        if (instance == null) {
            instance = new FirebaseClient();
        }
        return instance;
    }

    public DatabaseReference getFirebaseDatabase() {
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }
}
