package com.codepath.volunteerhero.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.fragments.CreateEventFragment;

import butterknife.BindView;

/**
 * Created by jan_spidlen on 10/10/17.
 */

public class CreateEventActivity extends BaseActivity{

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

        fragmentTransaction
                .add(R.id.outer_activity_layout, newFragment)
                .addToBackStack(null)
                .commit();
    }
}
