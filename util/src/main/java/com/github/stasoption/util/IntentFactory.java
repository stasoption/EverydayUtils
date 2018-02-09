package com.github.stasoption.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Pair;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Intent.ACTION_SENDTO;
import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;

/**
 * @author Nikita Simonov
 */

public final class IntentFactory {

    private IntentFactory() {
    }

    @NonNull
    public static Intent callIntent(@NonNull String phone) {
        return new Intent(Intent.ACTION_DIAL, Uri.parse(String.format("tel: %s", phone)));
    }

    public static Intent getSendEmailIntent(@Nullable String mailTo, @Nullable String mailCC,
                                            @Nullable String subject, @Nullable CharSequence body,
                                            @Nullable File attachment) {
        Intent intent = new Intent(ACTION_SENDTO);
        // intent.setType("text/plain");
        intent.setType("message/rfc822");
        if (mailTo == null) {
            mailTo = "";
        }
        intent.setData(Uri.parse("mailto:" + mailTo));
        if (isNotEmpty(mailCC)) {
            intent.putExtra(Intent.EXTRA_CC, new String[]{mailCC});
        }
        if (isNotEmpty(subject)) {
            intent.putExtra(EXTRA_SUBJECT, subject);
        }
        if (isNotEmpty(body)) {
            intent.putExtra(EXTRA_TEXT, body);
        }
        if (attachment != null) {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(attachment));
        }
        return intent;
    }

    public static void startRateAppIntent(@NonNull Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static Intent getSendSMSIntent(String msg) {
        Intent intent = new Intent(ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("sms_body", msg);
        return intent;
    }

    public static Intent getOpenUrlIntent(String webAddress) {
        return new Intent(ACTION_VIEW, Uri.parse(webAddress));
    }

    public static Intent getDialIntent(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        return intent;
    }

    public static Intent pickContact() {
        return pickContact(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
    }

    @Nullable
    public static Pair<Intent, String> dispatchTakePictureIntent(@NonNull Activity activity) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(activity);
            } catch (IOException ignored) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(activity,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                List<ResolveInfo> resInfoList = activity.getPackageManager()
                        .queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    activity.grantUriPermission(packageName, photoURI,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                return new Pair<>(takePictureIntent, photoFile.getAbsolutePath());
            }
        }
        return null;
    }

    @NonNull
    private static File createImageFile(@NonNull Activity activity) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    private static boolean isNotEmpty(@Nullable CharSequence text) {
        return !TextUtils.isEmpty(text);
    }

    private static Intent pickContact(String scope) {
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://com.android.contacts/contacts"));
        if (!TextUtils.isEmpty(scope)) {
            intent.setType(scope);
        }
        return intent;
    }
}
