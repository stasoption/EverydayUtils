package com.github.stasoption.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

/**
 * @author Grigoriy Dzhanelidze
 */
public final class PermissionUtils {
    private PermissionUtils() {
    }

    public static int getRequestCode(int reqCode) {
        return reqCode & 0xFF;
    }

    public static boolean isGranted(@NonNull int[] result) {
        for (int item : result) {
            if (item != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean isGranted(@NonNull Context context, @NonNull String[] permissions) {
        for (String permission : permissions) {
            if (!isGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isGranted(@NonNull Context context, @NonNull String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
}
