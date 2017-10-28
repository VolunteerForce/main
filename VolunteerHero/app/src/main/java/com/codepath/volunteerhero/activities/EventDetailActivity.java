package com.codepath.volunteerhero.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.fragments.EventDetailFragment;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.utils.VolunteerHeroConstants;

import org.parceler.Parcels;

/**
 * Created by dharinic on 10/17/17.
 */
public class EventDetailActivity extends BaseActivity {

    public static Intent getIntent(Context context, Event event) {
        return new Intent(context, EventDetailActivity.class)
                .putExtra(VolunteerHeroConstants.EXTRA_EVENT,
                        Parcels.wrap(event));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        if (savedInstanceState == null) {
            EventDetailFragment frag = EventDetailFragment.newInstance();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, frag, FragmentTransaction.class.getName());
            ft.commit();
        }
    }


}
