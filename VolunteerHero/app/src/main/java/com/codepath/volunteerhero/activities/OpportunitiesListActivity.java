package com.codepath.volunteerhero.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.adapters.EventFragmentPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main activity to browse list of opportunities
 */
public class OpportunitiesListActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    EventFragmentPagerAdapter mViewPagerAdapter;

    @BindView(R.id.fab)
    FloatingActionButton fab;

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
    }

    @OnClick(R.id.fab)
    public void startCreateActivity() {
        // TODO(jan.spidlen): Maybe start it for result.
        startActivity(new Intent(this.getApplicationContext(), CreateEventActivity.class));
    }

}
