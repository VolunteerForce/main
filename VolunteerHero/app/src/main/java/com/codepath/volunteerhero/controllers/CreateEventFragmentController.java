package com.codepath.volunteerhero.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.utils.NetworkUtils;
import com.google.android.gms.location.places.Place;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jan_spidlen on 10/15/17.
 */

public class CreateEventFragmentController implements DatePickerDialog.OnDateSetListener,
        TextWatcher {

    public Bitmap decodeImage(Intent data, File file) {

        final boolean isCamera;
        if (data == null || data.getData() == null) {
            isCamera = true;
        } else {
            final String action = data.getAction();
            if (action == null) {
                isCamera = false;
            } else {
                isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            }
        }
        Bitmap bmp = null;
        if (isCamera) {
            bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
        } else {
            Uri selectedImageUri = data == null ? null : data.getData();
            try {
                bmp = MediaStore.Images.Media.getBitmap(view.getActivity().getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
                NetworkUtils.showNonretryableError(view.getView(), R.string.cannot_decode_image);
            }
        }

        return bmp;
    }

    public interface View {
        String getTitle();
        String getDescription();
        void setCreateEventButtonEnabled(boolean enabled);

        Context getActivity();

        android.view.View getView();
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
