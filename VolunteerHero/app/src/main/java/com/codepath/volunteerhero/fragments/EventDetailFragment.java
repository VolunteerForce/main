package com.codepath.volunteerhero.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.volunteerhero.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dharinic on 10/17/17.
 */

public class EventDetailFragment extends Fragment {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    public static EventDetailFragment newInstance() {
        EventDetailFragment fragment = new EventDetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_create_event,
                container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateFragmentWithData();
    }


    @OnClick(R.id.btnSubscribe)
    void subscribe() {
        //TODO: (dharinic) save to firebase (use confirm dialog?)
    }

    @OnClick (R.id.ibShare)
    void shareEvent() {
        //TODO: (dharinic) share with facebook/email
    }

    void populateFragmentWithData() {
        //TODO: (dharinic) add data from event
    }

}
