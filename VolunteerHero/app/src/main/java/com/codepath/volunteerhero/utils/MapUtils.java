package com.codepath.volunteerhero.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.maps.android.ui.IconGenerator;
/**
 * Created by jan_spidlen on 10/15/17.
 */


public class MapUtils {



    public static void addPin(final GoogleMap map, final LatLng point, final String title, final String snippet) {

        BitmapDescriptor icon =
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        // Extract content from alert dialog

        addMarker(map, point, title, snippet, icon);
    }

    public static Marker addSpeechBubble(Context context, final GoogleMap map,
                                         final LatLng point, final String title,
                                         final String snippet) {

        IconGenerator iconGenerator = new IconGenerator(context);

        // Possible color options:
        // STYLE_WHITE, STYLE_RED, STYLE_BLUE, STYLE_GREEN, STYLE_PURPLE, STYLE_ORANGE
        iconGenerator.setStyle(IconGenerator.STYLE_GREEN);
        // Swap text here to live inside speech bubble
        Bitmap bitmap = iconGenerator.makeIcon(title);
        // Use BitmapDescriptorFactory to create the marker
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);

        return addMarker(map, point, title, snippet, icon);
    }

    public static Marker addMarker(final GoogleMap map, final LatLng point, final String title, final String snippet,
                                   final BitmapDescriptor icon) {
        Marker marker = map.addMarker(new MarkerOptions()
                .position(point)
                .title(title)
                .snippet(snippet)
                .icon(icon));
        marker.setDraggable(true);

        return marker;
    }
}