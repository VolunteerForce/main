package com.codepath.volunteerhero.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.View;

import com.codepath.volunteerhero.models.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by jan_spidlen on 10/19/17.
 */

public class Utils {

    static Random generator = new Random();

    public static String getUniqueImageFilename() {
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;
        for (int i = 0; i < 10; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString() + ".jpg";
    }

    public static File saveBitmapToTempImage(Bitmap bmp) {
        File fileToStoreImage = getExternalStorageRandomFile();
        fileToStoreImage.getParentFile().mkdirs();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileToStoreImage.getAbsolutePath());
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileToStoreImage;
    }

    public static File getExternalStorageRandomFile() {
        final File root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        return new File(root, "share_image_" + System.currentTimeMillis() + ".png");
    }

    public static String encodeToBase64(Bitmap image,
                                        Bitmap.CompressFormat compressFormat,
                                        int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }


    public static void showSnackBar(View v, String message, String buttonText,
                                  @Nullable View.OnClickListener action) {
        final Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_LONG);
        if (action == null) {
            action = view -> {
                snackbar.dismiss();
            };
        }
        snackbar.setAction(buttonText, action);

        snackbar.show();
    }

    public static String ellipsize(CharSequence input, int maxLength) {
        return ellipsize(input.toString(), maxLength);
    }

    public static String ellipsize(String input, int maxLength) {
        String ellip = "...";
        if (input == null || input.length() <= maxLength
                || input.length() < ellip.length()) {
            return input;
        }
        return input.substring(0, maxLength - ellip.length()).concat(ellip);
    }
}
