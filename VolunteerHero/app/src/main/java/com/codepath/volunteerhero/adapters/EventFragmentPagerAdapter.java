package com.codepath.volunteerhero.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.codepath.volunteerhero.fragments.OpportunitiesListFragment;
import com.codepath.volunteerhero.fragments.OpportunitiesMapFragment;


/**
 * Created by dharinic on 10/13/17.
 */

public class EventFragmentPagerAdapter extends FragmentStatePagerAdapter{

    private static final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Event List", "Map View"};
    private Context mContext;

    public EventFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag;
        if (position == 0) {
            frag = OpportunitiesListFragment.newInstance("List", "string2");
        } else {
            frag = OpportunitiesMapFragment.newInstance("Map", "string2");
        }
        return frag;
    }
}
