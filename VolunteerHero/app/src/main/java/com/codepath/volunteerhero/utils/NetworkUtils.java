package com.codepath.volunteerhero.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.codepath.volunteerhero.R;

/**
 * Created by jan_spidlen on 10/18/17.
 */

public class NetworkUtils {
    public static final String TAG = NetworkUtils.class.getSimpleName();

    public static void showRetryableError(@NonNull View v, @NonNull @StringRes int messageRes,
                                          @NonNull View.OnClickListener retryAction) {
        showRetryableError(v, v.getContext().getString(messageRes), retryAction);
    }

    public static void showRetryableError(@NonNull View v, String message,
                                          @NonNull View.OnClickListener retryAction) {
        Utils.showSnackBar(v, message, v.getContext().getString(R.string.retry), retryAction);
    }


    public static void showNonretryableError(@NonNull View v, String message) {
        Utils.showSnackBar(v, message, v.getContext().getString(R.string.ok), null);
    }

    public static void showNonretryableError(@NonNull View v, @NonNull @StringRes int messageRes) {
        showNonretryableError(v, v.getContext().getString(messageRes));
    }


    public static boolean isConnected(Context mContext) {
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }catch (Exception ex){
            Log.w(TAG, ex.getMessage());
        }
        return false;
    }
}
