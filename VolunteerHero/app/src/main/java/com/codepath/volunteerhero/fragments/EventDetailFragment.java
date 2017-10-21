package com.codepath.volunteerhero.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.utils.VolunteerHeroConstants;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dharinic on 10/17/17.
 */

public class EventDetailFragment extends Fragment {

    public static final String TAG = EventDetailFragment.class.getSimpleName();

    private Event mEvent;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvOrg)
    TextView tvOrg;

    @BindView(R.id.ivImage)
    ImageView ivImage;

    @BindView(R.id.tvLocation)
    TextView tvLocation;

    @BindView(R.id.tvDescription)
    TextView tvDescription;

    @BindView(R.id.tvWebsiteLink)
    TextView tvWebsite;

    public static EventDetailFragment newInstance() {
        EventDetailFragment fragment = new EventDetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_event_detail,
                container, false);

        mEvent = Parcels.unwrap(getActivity().getIntent().getParcelableExtra(VolunteerHeroConstants.EXTRA_EVENT));
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
        Log.e(TAG, ".. event = " + mEvent.title );
        Toast.makeText(getActivity(), "Got evnet " + mEvent.carrier.name , Toast.LENGTH_SHORT).show();

        tvTitle.setText(mEvent.title);
        tvLocation.setText(mEvent.getLocation());
        tvDescription.setText(mEvent.description);
        tvOrg.setText(mEvent.carrier.name);
       // tvWebsite.setText(event.carrier.);

     }

}
