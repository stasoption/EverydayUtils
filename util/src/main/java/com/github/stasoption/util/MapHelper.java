package com.github.stasoption.util;

import android.location.Location;

/**
 * Created by stasaverin on 13.11.17.
 */

public class MapHelper {

    private static final int KILOMETER = 1000; //meters

    public static float distanceBetween(double lat1, double lng1, double lat2, double lng2) {
        float[] distance = new float[10];
        Location.distanceBetween( lat1, lng1, lat2, lng2, distance);
        return distance[0] / KILOMETER;
    }
}
