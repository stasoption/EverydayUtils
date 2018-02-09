package com.github.stasoption.util;

import android.content.Context;
import android.location.LocationManager;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;


public final class AndroidUtil {

    private static final String TAG = ActionBarUtils.class.getSimpleName();

    public static boolean isProviderEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            if (lm != null) {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "gps is not enabled...");
        }

        try {
            if (lm != null) {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            Log.e(TAG, "network is not enabled...");
        }

        return !(!gps_enabled && !network_enabled);
    }


    public static void vibrate(@Nullable Context context, int duration) {
        if (context == null) {
            return;
        }
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator!=null && vibrator.hasVibrator()) {
            vibrator.vibrate(duration);
        }
    }
}
