package com.codepath.volunteerhero.controllers;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.codepath.volunteerhero.models.Event;
import com.google.android.gms.location.places.Place;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jan_spidlen on 10/15/17.
 */

public class CreateEventFragmentController implements DatePickerDialog.OnDateSetListener,
        TextWatcher {

    public interface View {
        String getTitle();
        String getDescription();
        void setCreateEventButtonEnabled(boolean enabled);
    }
    private final View view;
    private Context context;
    Place lastSelectedPlace;
    Calendar calendar;

    public CreateEventFragmentController(View view, Context context) {
        this.view = view;
        this.context = context;
        maybeEnableCreateButton();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Log.d("jenda", "date");
        calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);

        maybeEnableCreateButton();
    }

    public void setPlace(Place place) {
        this.lastSelectedPlace = place;
        maybeEnableCreateButton();
    }


    void maybeEnableCreateButton() {
        String title = view.getTitle(); //eventName.getText().toString();
        String description = view.getDescription();// eventDescription.getText().toString();
        final boolean enabled = lastSelectedPlace != null
                && calendar != null
                && !TextUtils.isEmpty(title)
                && !TextUtils.isEmpty(description);
        view.setCreateEventButtonEnabled(enabled);
    }

    public Event createEvent() {
        Event event = new Event();
        event.title = view.getTitle();
        event.description = view.getDescription();

        try {
            event.updateFromPlace(lastSelectedPlace, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return event;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        maybeEnableCreateButton();
    }
}
