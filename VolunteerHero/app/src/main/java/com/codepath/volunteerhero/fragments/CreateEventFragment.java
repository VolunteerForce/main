package com.codepath.volunteerhero.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.models.Event;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.IOException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jan_spidlen on 10/12/17.
 */

public class CreateEventFragment extends Fragment {

    @BindView(R.id.upload_cover_image_button)
    ImageView coverPhoto;
    @BindView(R.id.event_name_edit_text)
    EditText eventName;
    @BindView(R.id.event_description_edit_text)
    EditText eventDescription;

    @BindView(R.id.event_type_button)
    Button eventTypeButton;
    @BindView(R.id.event_address_button)
    Button eventAddressButton;
    @BindView(R.id.event_date_button)
    Button eventDate;
    @BindView(R.id.create_event_button)
    Button createEventButton;

    final static int PLACE_PICKER_REQUEST = 1;
    Place lastSelectedPlace;
    Date lastSelectedEventDate;

    public static CreateEventFragment newInstance() {
        CreateEventFragment fragment = new CreateEventFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this.getActivity(), data);
                lastSelectedPlace = place;
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this.getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
//
//
    @OnClick(R.id.event_address_button)
    void selectAddress() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        Log.d("jenda", "selectAddress ");
        try {
            startActivityForResult(builder.build(this.getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.create_event_button)
    void createEvent() {
        Event event = new Event();
        event.title = eventName.getText().toString();
        event.description = eventDescription.getText().toString();

        try {
            event.updateFromPlace(lastSelectedPlace, this.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
