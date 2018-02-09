package com.github.stasoption.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import javax.crypto.Cipher;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


@TargetApi(Build.VERSION_CODES.M)
public final class FingerprintHelper extends FingerprintManager.AuthenticationCallback {

    private CancellationSignal mCancellationSignal;

    @Nullable
    private FingerprintHelperListener mListener;


    public void setListener(@Nullable FingerprintHelperListener listener) {
        mListener = listener;
    }

    public void startAuth(@Nullable Context context, @NonNull FingerprintManager.CryptoObject cryptoObject) {

        if (context == null || ActivityCompat
                .checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PERMISSION_GRANTED) {
            return;
        }
        FingerprintManager manager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        mCancellationSignal = new CancellationSignal();
        if (manager != null) {
            manager.authenticate(cryptoObject, mCancellationSignal, 0, this, null);
        }
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        if (errorCode != FingerprintManager.FINGERPRINT_ERROR_CANCELED && mListener != null) {
            mListener.onAuthError((String) errString);
        }
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        if (mListener != null) {
            mListener.onAuthHelp((String) helpString);
        }
    }

    @Override
    public void onAuthenticationSucceeded(@NonNull FingerprintManager.AuthenticationResult result) {
        if (mListener != null) {
            mListener.onAuthSuccess(result.getCryptoObject().getCipher());
        }
    }

    @Override
    public void onAuthenticationFailed() {
        if (mListener != null) {
            mListener.onAuthFailed();
        }
    }

    public void cancel() {
        if (mCancellationSignal != null) {
            mCancellationSignal.cancel();
        }
    }

    public interface FingerprintHelperListener {

        void onAuthSuccess(@NonNull Cipher cipher);

        void onAuthFailed();

        void onAuthError(@NonNull String errString);

        void onAuthHelp(@NonNull String help);
    }

}
