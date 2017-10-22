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
import com.codepath.volunteerhero.VolunteerHeroApplication;
import com.codepath.volunteerhero.data.EventDataProvider;
import com.codepath.volunteerhero.database.FirebaseDBHelper;
import com.codepath.volunteerhero.imgur.ImageResponse;
import com.codepath.volunteerhero.imgur.Upload;
import com.codepath.volunteerhero.imgur.UploadService;
import com.codepath.volunteerhero.models.Carrier;
import com.codepath.volunteerhero.models.Contact;
import com.codepath.volunteerhero.models.Event;
import com.codepath.volunteerhero.utils.NetworkUtils;
import com.codepath.volunteerhero.utils.Utils;
import com.google.android.gms.location.places.Place;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.codepath.volunteerhero.utils.Utils.saveBitmapToTempImage;

/**
 * Created by jan_spidlen on 10/15/17.
 */

public class CreateEventFragmentController implements DatePickerDialog.OnDateSetListener,
        TextWatcher, Callback<ImageResponse> {

    public interface View {
        String getTitle();
        String getDescription();
        void setCreateEventButtonEnabled(boolean enabled);

        Context getActivity();

        android.view.View getView();
        void imageUploadFailed();
        void eventCreatedSuccessfully();

        void onDatePicked(Calendar calendar);

        List<String> getSelectedTopics();
    }
    private final View view;
    private Context context;
    private Bitmap bitmap;
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
        this.view.onDatePicked(calendar);
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


    public void startEventCreation() {
        if (bitmap != null) {
            uploadImage(bitmap);
        } else {
            createEventAndSave(null);
        }
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

        if (isCamera) {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        } else {
            Uri selectedImageUri = data == null ? null : data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(
                        view.getActivity().getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
                NetworkUtils.showNonretryableError(view.getView(), R.string.cannot_decode_image);
            }
        }

        return bitmap;
    }


    public void uploadImage(File f) {
        UploadService uploadService = new UploadService(view.getActivity());
        Upload upload = new Upload();
        upload.image = f;
        uploadService.execute(upload, this);

    }

    public void uploadImage(Bitmap bmp) {
        File f = Utils.saveBitmapToTempImage(bmp);
        uploadImage(f);
    }

    @Override
    public void success(ImageResponse imageResponse, Response response) {
        Log.d("jenda", "imageResponse " + imageResponse.toString());
        if (imageResponse.success) {
            createEventAndSave(imageResponse.data.link);
        } else {
            view.imageUploadFailed();
        }
    }

    Random rnd = new Random();


    public Event createEventAndSave(String link) {
        Event event = new Event();
        event.title = view.getTitle();
        event.description = view.getDescription();
        event.id = "id:" + rnd.nextInt();
        event.eventHeaderImageUrl = link;

        try {
            event.updateFromPlace(lastSelectedPlace, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        event.topics = view.getSelectedTopics();
        event.carrier = new Carrier();
        event.createdAt = event.updatedAt = new Date();
        event.creator = VolunteerHeroApplication.getLoggedInUser();

        event.contact = new Contact();
        event.contact.email = VolunteerHeroApplication.getLoggedInUser().email;
        event.contact.name = VolunteerHeroApplication.getLoggedInUser().name;

        Log.d("jenda", "event.creator " + event.creator );
        event = FirebaseDBHelper.getInstance().addEvent(event);
        EventDataProvider.getInstance().addOrUpdateData(event);
        
        view.eventCreatedSuccessfully();
        return event;
    }

    @Override
    public void failure(RetrofitError error) {
        view.imageUploadFailed();
        Log.d("jenda", "failure " + error.toString());
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
