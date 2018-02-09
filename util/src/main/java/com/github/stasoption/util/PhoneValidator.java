package com.github.stasoption.util;

import android.support.annotation.Nullable;


public class PhoneValidator {
    private static final String RUS_PHONE = "((8|\\+7)[- ]?)?\\(?\\d{3}\\)?[- ]?\\d{1}[- ]?\\d{1}[- ]?\\d{1}[- ]?\\d{1}[- ]?\\d{1}[- ]?\\d{1}[- ]?\\d{1}";

    public static boolean validateRusPhone(@Nullable String phone) {
        return phone != null && phone.matches(RUS_PHONE);
    }
}
