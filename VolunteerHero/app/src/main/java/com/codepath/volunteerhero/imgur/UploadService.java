package com.codepath.volunteerhero.imgur;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.codepath.volunteerhero.utils.NetworkUtils;

import java.lang.ref.WeakReference;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by jan_spidlen on 10/21/17.
 */

public class UploadService {
    public final static String TAG = UploadService.class.getSimpleName();

    private WeakReference<Context> context;

    public UploadService(Context context) {
        this.context = new WeakReference<>(context);
    }

    public void execute(@NonNull final Upload upload,
                        @Nullable final Callback<ImageResponse> callback) {

        if (!NetworkUtils.isConnected(context.get())) {
            //Callback will be called, so we prevent a unnecessary notification
            callback.failure(null);
            return;
        }

//        final NotificationHelper notificationHelper = new NotificationHelper(mContext.get());
//        notificationHelper.createUploadingNotification();

        RestAdapter restAdapter = buildRestAdapter();

        restAdapter.create(ImgurAPI.class).postImage(
                Constants.getClientAuth(),
                upload.title,
                upload.description,
                upload.albumId,
                null,
//                upload.imageBase64,
                new TypedFile("image/*", upload.image),
                new Callback<ImageResponse>() {
                    @Override
                    public void success(ImageResponse imageResponse, Response response) {
                        if (callback != null){
                            callback.success(imageResponse, response);
                        }
                        if (response == null) {
                            /*
                             Notify image was NOT uploaded successfully
                            */
//                            notificationHelper.createFailedUploadNotification();
                            return;
                        }
                        /*
                        Notify image was uploaded successfully
                        */
                        if (imageResponse.success) {
//                            notificationHelper.createUploadedNotification(imageResponse);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (callback != null) {
                            callback.failure(error);
                        }
//                        notificationHelper.createFailedUploadNotification();
                    }
                });
    }

    private RestAdapter buildRestAdapter() {
        RestAdapter imgurAdapter = new RestAdapter.Builder()
                .setEndpoint(ImgurAPI.server)
                .build();

        /*
        Set rest adapter logging if we're already logging
        */
        if (Constants.LOGGING) {
            imgurAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        }
        return imgurAdapter;
    }
}
