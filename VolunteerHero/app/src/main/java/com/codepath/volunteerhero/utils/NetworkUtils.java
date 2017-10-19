package com.codepath.volunteerhero.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.codepath.volunteerhero.R;

/**
 * Created by jan_spidlen on 10/18/17.
 */

public class NetworkUtils {

    public static void showRetryableError(@NonNull View v, @NonNull @StringRes int messageRes,
                                          @Nullable View.OnClickListener retryAction) {
        showError(v, messageRes, R.string.retry, retryAction);
    }

    public static void showNonretryableError(@NonNull View v, @NonNull @StringRes int messageRes) {
        showError(v, messageRes, R.string.ok, null);
    }

    private static void showError(View v, @StringRes int messageRes,
                                 @StringRes int buttonTextRes,
                                 @Nullable View.OnClickListener retryAction) {
        final Snackbar snackbar = Snackbar.make(v, messageRes, Snackbar.LENGTH_LONG);
        if (retryAction == null) {
            retryAction = view -> {
                snackbar.dismiss();
            };
        }
        snackbar.setAction(buttonTextRes, retryAction);

        snackbar.show();
    }
}
