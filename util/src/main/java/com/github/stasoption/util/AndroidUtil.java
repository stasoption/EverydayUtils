package com.github.stasoption.util;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.os.Vibrator;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;


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

    public static void setStatusBarColor(@NonNull Activity activity, @ColorRes int colorRes) {
        Window window = activity.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, colorRes));
        }
    }

    public static int getActionBarHeight(@NonNull Context context){
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }
}
