package com.codepath.volunteerhero.utils;

import android.graphics.Bitmap;
import android.util.Base64;

import com.codepath.volunteerhero.models.User;

import java.io.ByteArrayOutputStream;
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

    public static String encodeToBase64(Bitmap image,
                                        Bitmap.CompressFormat compressFormat,
                                        int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
