package com.codepath.volunteerhero.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.VolunteerHeroApplication;
import com.codepath.volunteerhero.database.FirebaseDBHelper;
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

    @BindView(R.id.tvNumVacancies)
    TextView tvNumVacancies;

    @BindView(R.id.tvActivities)
    TextView tvActivities;

    @BindView(R.id.tvContactEmail)
    TextView tvContactEmail;

    @BindView(R.id.tvContactName)
    TextView tvContactName;

    public static EventDetailFragment newInstance() {
        EventDetailFragment fragment = new EventDetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_event_detail,
                container, false);

        mEvent = (Event)Parcels.unwrap(getActivity().getIntent().getParcelableExtra(VolunteerHeroConstants.EXTRA_EVENT));
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
        //TODO: use confirm dialog?)
        FirebaseDBHelper helper = FirebaseDBHelper.getInstance();
        helper.addUsersSubscribedEvent(VolunteerHeroApplication.getLoggedInUser(), mEvent);
    }

    @OnClick (R.id.ibShare)
    void shareEvent() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, mEvent.title);
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    void populateFragmentWithData() {
        Log.e(TAG, ".. event = " + mEvent.title );

        tvTitle.setText(mEvent.title);
        tvLocation.setText(mEvent.getLocation());
        Glide.with(getActivity()).load(mEvent.getImageUrl())
                .into(ivImage);
        tvNumVacancies.setText(String.valueOf(mEvent.vacancies));
        tvOrg.setText(mEvent.carrier.name);
        tvActivities.setText(mEvent.getActivities());

        tvContactEmail.setText(mEvent.contact.getEmail());
        tvContactName.setText(mEvent.contact.getName());

     }

}
