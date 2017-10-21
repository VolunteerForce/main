package com.codepath.volunteerhero;

import android.support.multidex.MultiDexApplication;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Main application
 *
 * @author tejalpar
 * Created on 10/12/17.
 */
public class VolunteerHeroApplication extends MultiDexApplication {
    private static FirebaseUser loggedInUser;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public static FirebaseUser getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(FirebaseUser firebaseUser) {
        loggedInUser = firebaseUser;
    }
}
