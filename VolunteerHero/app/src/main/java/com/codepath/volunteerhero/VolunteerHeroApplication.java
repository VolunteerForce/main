package com.codepath.volunteerhero;

import android.app.Application;

import com.google.firebase.auth.FirebaseUser;

/**
 * Main application
 *
 * @author tejalpar
 * Created on 10/12/17.
 */
public class VolunteerHeroApplication extends Application {
    private static FirebaseUser loggedInUser;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static FirebaseUser getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(FirebaseUser firebaseUser) {
        loggedInUser = firebaseUser;
    }
}
