package com.codepath.volunteerhero.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.adapters.TopicAdapter;
import com.codepath.volunteerhero.controllers.CreateEventFragmentController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.codepath.volunteerhero.models.Topics;
import com.codepath.volunteerhero.utils.NetworkUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * Created by jan_spidlen on 10/12/17.
 */

public class CreateEventFragment extends Fragment implements CreateEventFragmentController.View {

//    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int IMAGE_OR_CAMERA_REQUEST_CODE = 2;
    private static final int PLACE_PICKER_REQUEST = 3;

    private File file;
    private Bitmap bitmap;

    @BindView(R.id.upload_cover_image_button)
    ImageButton coverPhotoButton;
    @BindView(R.id.event_name_edit_text)
    EditText eventName;
    @BindView(R.id.event_description_edit_text)
    EditText eventDescription;

    @BindView(R.id.event_address_button)
    Button eventAddressButton;
    @BindView(R.id.event_date_button)
    Button eventDateButton;
    @BindView(R.id.create_event_button)
    Button createEventButton;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.progressBarLayout)
    FrameLayout progressBarLayout;

    @BindView(R.id.topics_recycler_view)
    RecyclerView topicsRecyclerView;

    CreateEventFragmentController controller;
    TopicAdapter topicAdapter;

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
//        hideSpinnerEnableCreateButton();

        topicAdapter = new TopicAdapter(this.getContext(),Topics.getAvailableTopics());
        topicsRecyclerView.setAdapter(topicAdapter);
        topicAdapter.notifyDataSetChanged();
        return view;
    }

    private void setUpTextListeners() {
        eventName.addTextChangedListener(controller);
        eventDescription.addTextChangedListener(controller);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("jenda", "onActivityResult " + resultCode + " " + requestCode);

        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("jenda", "event cancelled " + resultCode + " " + requestCode);
            return;
        }
        if (resultCode != Activity.RESULT_OK) {
            NetworkUtils.showNonretryableError(this.getView(), "Something went wrong " + resultCode);
            return;
        }

        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                Place place = PlacePicker.getPlace(this.getActivity(), data);
                eventAddressButton.setText(place.getAddress());
                controller.setPlace(place);
                break;
            case IMAGE_OR_CAMERA_REQUEST_CODE:
                Bitmap bmp = controller.decodeImage(data, file);
                coverPhotoButton.setImageBitmap(bmp);
                break;
        }
    }

    @OnClick(R.id.event_address_button)
    void selectAddress() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this.getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            NetworkUtils.showNonretryableError(this.getView(), R.string.cannot_select_address);
        }
    }

    @OnClick(R.id.create_event_button)
    void createEvent() {
        showSpinnerAndDisableCreateButton();
        controller.startEventCreation();
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

    @OnClick(R.id.test_button)
    void test() {
        NetworkUtils.showRetryableError(this.getView(), R.string.test_error_message_1, v -> {
            NetworkUtils.showNonretryableError(this.getView(), R.string.test_error_message_2);
        });
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

    @Override
    public void imageUploadFailed() {
        hideSpinnerEnableCreateButton();
        NetworkUtils.showRetryableError(this.getView(),
                R.string.cover_photo_upload_failed, (v) -> {
                    controller.startEventCreation();
                });
    }

    @Override
    public void eventCreatedSuccessfully() {
        hideSpinnerEnableCreateButton();
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onDatePicked(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        this.eventDateButton.setText(format.format(calendar.getTime()));
    }

    @Override
    public List<String> getSelectedTopics() {
        List<String> topics = new ArrayList<>();
        topics.addAll(topicAdapter.getSelectedTopics());
        return topics;
    }

    void showSpinnerAndDisableCreateButton() {
        createEventButton.setEnabled(false);
        progressBarLayout.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    void hideSpinnerEnableCreateButton() {
        createEventButton.setEnabled(true);
        progressBarLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.upload_cover_image_button)
    void openImageIntent() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                String[] perms = {
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };
                int permsRequestCode = 200;
                requestPermissions(perms, permsRequestCode);
            return;
        }

        // Determine Uri of camera image to save.
        final File root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        file = new File(root, "share_image_" + System.currentTimeMillis() + ".png");

        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            NetworkUtils.showNonretryableError(this.getView(), "Dir not created");
    }
        Uri outputFileUri = FileProvider.getUriForFile(getActivity(),
                "com.codepath.volunteerhero", file);

        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getActivity().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        startActivityForResult(chooserIntent, IMAGE_OR_CAMERA_REQUEST_CODE);
    }
}
