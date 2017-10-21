package com.codepath.volunteerhero.fragments;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.codepath.volunteerhero.R;
import com.codepath.volunteerhero.controllers.CreateEventFragmentController;
import com.codepath.volunteerhero.data.EventDataProvider;
import com.codepath.volunteerhero.models.Event;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.codepath.volunteerhero.utils.NetworkUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * Created by jan_spidlen on 10/12/17.
 */

public class CreateEventFragment extends Fragment implements CreateEventFragmentController.View {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int IMAGE_OR_CAMERA_REQUEST_CODE = 2;

    @BindView(R.id.upload_cover_image_button)
    ImageButton coverPhotoButton;
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

        Log.d("jenda", "onActivityResult " + resultCode + " " + requestCode);

        if (resultCode != RESULT_OK) {
            NetworkUtils.showNonretryableError(this.getView(), "Something went wrong " + resultCode);
            return;
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                final Place place = PlacePicker.getPlace(this.getActivity(), data);
                controller.setPlace(place);
            }
        } else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri imageUri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };


            Cursor cursor = this.getActivity().getContentResolver().query(imageUri,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

//            ImageView imageView = (ImageView) findViewById(R.id.imgView);

//            this.getActivity().getB
            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                //this.getActivity().getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//            imageView.setImageBitmap(bmp);

        }
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_OR_CAMERA_REQUEST_CODE) {

                Log.d("jenda", "bmp inside ");
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
//                File f; f.doce
                Log.d("jenda", "path " + outputFileUri.getPath());

                Log.d("jenda", "outputFileUri exists " + new File(outputFileUri.getPath()).exists());

                Log.d("jenda", "   data.getExtras().get(\"data\"); " + data.getData());
//                Log.d("jenda", "path " + PathUtil.getPath(this.getContext(),outputFileUri));

                BitmapFactory.decodeFile(outputFileUri.getPath());
//                BitmapFactory.decodeUr
                if (isCamera) {
                    selectedImageUri = Uri.fromFile(new File(outputFileUri.getPath())); ;
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }

                Bitmap bmp = null;
                try {
                    bmp =
                            BitmapFactory.decodeFile(outputFileUri.getPath());
//                    bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    //this.getActivity().getBitmapFromUri(selectedImage);
                    Log.d("jenda", "bmp " + bmp.getByteCount());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
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

    @OnClick(R.id.test_button)
    void test() {
        NetworkUtils.showRetryableError(this.getView(), R.string.test_error_message_1, v -> {
            NetworkUtils.showNonretryableError(this.getView(), R.string.test_error_message_2);
        });
    }


    @OnClick(R.id.upload_cover_image_button)
    void uploadCoverPhoto() {

        openImageIntent();
//        Intent i = new Intent(
//                Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            Uri imageUri = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//
//            Cursor cursor = this.getActivity().getContentResolver().query(imageUri,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
////            ImageView imageView = (ImageView) findViewById(R.id.imgView);
//
////            this.getActivity().getB
//            Bitmap bmp = null;
//            try {
//                bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
//                //this.getActivity().getBitmapFromUri(selectedImage);
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
////            imageView.setImageBitmap(bmp);
//
//        }
//
//
//    }


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

    private Uri outputFileUri;

    private String fullPath;

    private void openImageIntent() {

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                String[] perms = {  Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE };
                int permsRequestCode = 200;
                requestPermissions(perms, permsRequestCode);
            return;
        }

        // Determine Uri of camera image to save.
        final File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File root = Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES);
        File file = new File(root, "share_image_" + System.currentTimeMillis() + ".png");


//        file.cre
        if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            NetworkUtils.showNonretryableError(this.getView(), "Dir not created");
        }
//        final String fname = Utils.getUniqueImageFilename();
//        final File sdImageMainDirectory = new File(root, "test_file.jpg");
//        outputFileUri = Uri.fromFile(sdImageMainDirectory);
        outputFileUri = FileProvider.getUriForFile(getActivity(), "com.codepath.volunteerhero", file);

        Log.d("jenda", "absolute path " + outputFileUri.getPath());
//        fullPath = sdImageMainDirectory.getAbsolutePath();
        // Camera.
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
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        // set the image file name

        startActivityForResult(intent, IMAGE_OR_CAMERA_REQUEST_CODE);
    }
}
