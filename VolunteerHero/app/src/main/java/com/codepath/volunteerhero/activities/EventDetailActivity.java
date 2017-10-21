package com.codepath.volunteerhero.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.fragments.EventDetailFragment;

/**
 * Created by dharinic on 10/17/17.
 */
public class EventDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        if (savedInstanceState == null) {
            EventDetailFragment frag = EventDetailFragment.newInstance();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, frag);
            ft.commit();
        }
    }


}
