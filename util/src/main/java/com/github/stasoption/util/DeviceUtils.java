package com.github.stasoption.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;


public final class DeviceUtils {
    private DeviceUtils() {
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
}
