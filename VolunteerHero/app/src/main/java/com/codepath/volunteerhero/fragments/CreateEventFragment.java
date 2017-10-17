package com.codepath.volunteerhero.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.controllers.CreateEventFragmentController;
import com.codepath.volunteerhero.data.EventDataProvider;
import com.codepath.volunteerhero.models.Event;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.codepath.volunteerhero.storage.LocalStorage;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.zxing.common.StringUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jan_spidlen on 10/12/17.
 */

public class CreateEventFragment extends Fragment implements CreateEventFragmentController.View {

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

    CreateEventFragmentController controller;

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

        controller = new CreateEventFragmentController(this, this.getContext());
        setUpTextListeners();
        return view;
    }

    private void setUpTextListeners() {
        eventName.addTextChangedListener(controller);
        eventDescription.addTextChangedListener(controller);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                final Place place = PlacePicker.getPlace(this.getActivity(), data);
                controller.setPlace(place);
            }
        }
    }

    @OnClick(R.id.event_address_button)
    void selectAddress() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
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
        Event event = controller.createEvent();
        EventDataProvider.getInstance().addOrUpdateData(event);
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @OnClick(R.id.event_date_button)
    void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                controller,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public String getTitle() {
        return eventName.getText().toString();
    }

    @Override
    public String getDescription() {
        return eventDescription.getText().toString();
    }

    @Override
    public void setCreateEventButtonEnabled(boolean enabled) {
        createEventButton.setEnabled(enabled);
    }
}
