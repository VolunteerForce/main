package com.codepath.volunteerhero.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.fragments.CreateEventFragment;

import butterknife.BindView;

/**
 * Created by jan_spidlen on 10/10/17.
 */

public class CreateEventActivity extends BaseActivity implements FragmentManager.OnBackStackChangedListener {

    @BindView(R.id.outer_activity_layout)
    FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_event);
        transitionToFragment(CreateEventFragment.newInstance());
    }



    private void transitionToFragment(Fragment newFragment) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                .setCustomAnimations(R.anim.enter_bottom, R.anim.fragment_slide_delay,
//                        0, R.anim.exit_bottom);

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        fragmentTransaction
                .add(R.id.outer_activity_layout, newFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackStackChanged() {
        Log.d("jenda", " getSupportFragmentManager().getBackStackEntryCount()" +
                getSupportFragmentManager().getBackStackEntryCount());

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            // Need better way of closing activity.
            this.finish();
        }
    }
}
