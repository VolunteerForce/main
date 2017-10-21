package com.codepath.volunteerhero;

import android.app.Application;

import com.codepath.volunteerhero.models.User;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Main application
 *
 * @author tejalpar
 * Created on 10/12/17.
 */
public class VolunteerHeroApplication extends Application {
    private static User loggedInUser;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(User loggedInUser) {
        VolunteerHeroApplication.loggedInUser = loggedInUser;
    }
}
