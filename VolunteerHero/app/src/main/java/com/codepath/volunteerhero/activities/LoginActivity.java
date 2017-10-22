package com.codepath.volunteerhero.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.VolunteerHeroApplication;
import com.codepath.volunteerhero.database.FirebaseDBHelper;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.models.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Facebook login activity
 *
 * @author tejalpar
 * Created on 10/11/17.
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;

    @BindView(R.id.fb_login_button) LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("email", "public_profile");
        //addPermissions();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "facebook:onError", exception);
            }
        });

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        FirebaseDBHelper.getInstance().getUser(currentUser, new FirebaseDBHelper.DataChangeEventListener() {
            @Override
            public void onUserDataUpdated(User user) {

            }

            @Override
            public void onEventDataUpdated(Event event) {

            }

            @Override
            public void onUserInfoAvailable(User loggedInUser) {
                VolunteerHeroApplication.setLoggedInUser(loggedInUser);
                showOpportunitiesListActivity();
            }

            @Override
            public void onUserInfoNotFound(FirebaseUser firebaseUser) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        onLoginSuccess();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }

                });
    }

    private void showOpportunitiesListActivity() {
        Intent intent = new Intent(this, OpportunitiesListActivity.class);
        startActivity(intent);
    }

    private void onLoginSuccess() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        FirebaseDBHelper.getInstance().getUser(user, new FirebaseDBHelper.DataChangeEventListener() {
            @Override
            public void onUserDataUpdated(User user) {

            }

            @Override
            public void onEventDataUpdated(Event event) {

            }

            @Override
            public void onUserInfoAvailable(User loggedInUser) {
                VolunteerHeroApplication.setLoggedInUser(loggedInUser);
                showOpportunitiesListActivity();
            }

            @Override
            public void onUserInfoNotFound(FirebaseUser firebaseUser) {
                User loggedInUser = FirebaseDBHelper.getInstance().saveUser(user);
                VolunteerHeroApplication.setLoggedInUser(loggedInUser);
                showOpportunitiesListActivity();
            }
        });
    }

    private void addPermissions() {
        List<String> permissions = new ArrayList<>();
        permissions.add("public_profile");
        permissions.add("pages_show_list");
        LoginManager.getInstance().logInWithReadPermissions(this, permissions);
    }
}
