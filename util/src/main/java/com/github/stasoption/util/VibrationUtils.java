package com.github.stasoption.util;

import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.Nullable;

public final class VibrationUtils {

    private VibrationUtils() {
    }

    public static void vibrateDuration(@Nullable Context context, int duration) {
        if (context == null) {
            return;
        }
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator!=null && vibrator.hasVibrator()) {
            vibrator.vibrate(duration);
        }

    }

}
