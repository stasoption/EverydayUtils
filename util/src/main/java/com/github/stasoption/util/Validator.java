package com.github.stasoption.util;

import android.support.annotation.Nullable;

public class Validator {

    private static final String RUS_PHONE = "((8|\\+7)[- ]?)?\\(?\\d{3}\\)?[- ]?\\d{1}[- ]?\\d{1}[- ]?\\d{1}[- ]?\\d{1}[- ]?\\d{1}[- ]?\\d{1}[- ]?\\d{1}";

    public static boolean validateRusPhone(@Nullable String phone) {
        return phone != null && phone.matches(RUS_PHONE);
    }

    public static boolean validateEmail(@Nullable String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
