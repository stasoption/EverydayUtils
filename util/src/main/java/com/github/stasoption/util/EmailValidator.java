package com.github.stasoption.util;

import android.support.annotation.Nullable;

public class EmailValidator {
    public static boolean validateEmail(@Nullable String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
