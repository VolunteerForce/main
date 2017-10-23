package com.codepath.volunteerhero.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.VolunteerHeroApplication;
import com.codepath.volunteerhero.adapters.EventFragmentPagerAdapter;
import com.codepath.volunteerhero.database.FirebaseDBHelper;
import com.codepath.volunteerhero.fragments.SettingsDialogFragment;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.models.User;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main activity to browse list of opportunities
 */
public class OpportunitiesListActivity extends BaseActivity {
    public static String FILTER_DIALOG_TAG = "FILTER_DIALOG";

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    EventFragmentPagerAdapter mViewPagerAdapter;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigation_view)
    NavigationView drawerNavView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunities_list);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        //create adapter
        mViewPagerAdapter = new EventFragmentPagerAdapter(getSupportFragmentManager(), this);

        mViewPager.setAdapter(mViewPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);

        // Setup drawer view
        setupDrawerContent(drawerNavView);

        // setup menu icon
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View headerView = drawerNavView.getHeaderView(0);
        TextView userNameText = headerView.findViewById(R.id.nav_user_name);
        userNameText.setText(VolunteerHeroApplication.getLoggedInUser().name);
    }

    @OnClick(R.id.fab)
    public void startCreateActivity() {
        // TODO(jan.spidlen): Maybe start it for result.
        startActivity(new Intent(this.getApplicationContext(), CreateEventActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_filter:
                showSettingsDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.nav_filter_by_name:
                break;
            case R.id.nav_filter_by_category:
                break;
            case R.id.nav_logout_user:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                showLoginActivity();
                break;
            default:
                drawerLayout.closeDrawers();
                break;
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    private void showLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void showSettingsDialog() {
        SettingsDialogFragment filterSettings = SettingsDialogFragment.newInstance("Filter options");
        filterSettings.show(getFragmentManager(), FILTER_DIALOG_TAG);
    }

    // Helper method to fetch all users & events
    private void getSubscribedEvents() {
        List<User> users = new ArrayList<>();
        FirebaseDBHelper.getInstance().getUsersSubscribedEvents(VolunteerHeroApplication.getLoggedInUser(), new FirebaseDBHelper.DataChangeEventListener() {
            @Override
            public void onUserDataUpdated(User user) {
                users.add(user);
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

    private void getEventList() {
        List<Event> events = new ArrayList<>();
        FirebaseDBHelper.getInstance().getEventList(new FirebaseDBHelper.DataChangeEventListener() {
            @Override
            public void onUserDataUpdated(User user) {
            }

            @Override
            public void onEventDataUpdated(Event event) {
                events.add(event);
            }

            @Override
            public void onUserInfoAvailable(User loggedInUser) {

            }

            @Override
            public void onUserInfoNotFound(FirebaseUser firebaseUser) {

            }
        });
    }
}
