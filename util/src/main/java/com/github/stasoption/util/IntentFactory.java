package com.github.stasoption.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
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

public final class IntentFactory {

    private IntentFactory() {
    }

    @NonNull
    public static Intent callPhone(@NonNull String phone) {
        return new Intent(Intent.ACTION_DIAL, Uri.parse(String.format("tel: %s", phone)));
    }


    public static Intent sendEmail(@Nullable String mailTo, @Nullable String mailCC,
                                            @Nullable String subject, @Nullable CharSequence body,
                                            @Nullable File attachment) {
        Intent intent = new Intent(ACTION_SENDTO);
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

    public static void rateApp(@NonNull Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static Intent sendSMS(String msg) {
        Intent intent = new Intent(ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        intent.putExtra("sms_body", msg);
        return intent;
    }

    public static Intent shareText(String subject, String text) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        if (!TextUtils.isEmpty(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        return intent;
    }

    public static Intent openUrl(String webAddress) {
        return new Intent(ACTION_VIEW, Uri.parse(webAddress));
    }

    public static Intent dialPhone(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        return intent;
    }

    public static Intent pickContact() {
        return pickContact(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
    }

    @Nullable
    public static Pair<Intent, String> dispatchTakePicture(@NonNull Activity activity) {
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

    @Nullable
    public static Intent photoCapture(@NonNull Activity activity) {
        File photoFile = null;
        try {
            photoFile = createImageFile(activity);
        } catch (IOException ignored) {
            return null;
        }

        Uri uri = Uri.fromFile(photoFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    public static Intent openVideo(File file) {
        return openVideo(Uri.fromFile(file));
    }

    public static Intent openVideo(String file) {
        return openVideo(new File(file));
    }

    public static Intent openVideo(Uri uri) {
        return openMedia(uri, "video/*");
    }


    public static Intent openAudio(File file) {
        return openAudio(Uri.fromFile(file));
    }

    public static Intent openAudio(String file) {
        return openAudio(new File(file));
    }


    public static Intent openAudio(Uri uri) {
        return openMedia(uri, "audio/*");
    }


    public static Intent openImage(String file) {
        return openImage(new File(file));
    }


    public static Intent openImage(File file) {
        return openImage(Uri.fromFile(file));
    }


    public static Intent openImage(Uri uri) {
        return openMedia(uri, "image/*");
    }


    public static Intent openText(String file) {
        return openText(new File(file));
    }


    public static Intent openText(File file) {
        return openText(Uri.fromFile(file));
    }


    public static Intent openText(Uri uri) {
        return openMedia(uri, "text/plain");
    }

    public static Intent pickFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        return intent;
    }

    public static Intent pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }

    public static boolean isCropAvailable(Context context) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        return isIntentAvailable(context, intent);
    }

    /**
     * Crop image. Before using, cropImage requires especial check that differs from
     * {@link #isIntentAvailable(android.content.Context, android.content.Intent)}
     * see {@link #isCropAvailable(android.content.Context)} instead
     *
     * @param context Application context
     * @param image   Image that will be used for cropping. This image is not changed during the cropImage
     * @param outputX Output image width
     * @param outputY Output image height
     * @param aspectX Crop frame aspect X
     * @param aspectY Crop frame aspect Y
     * @param scale   Scale or not cropped image if output image and cropImage frame sizes differs
     * @return Intent with <code>data</code>-extra in <code>onActivityResult</code> which contains result as a
     * {@link android.graphics.Bitmap}. See demo app for details
     */
    public static Intent cropImage(Context context, File image, int outputX, int outputY, int aspectX, int aspectY, boolean scale) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);
        ResolveInfo res = list.get(0);

        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("scale", scale);
        intent.putExtra("return-data", true);
        intent.setData(Uri.fromFile(image));

        intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
        return intent;
    }


    public static boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    private static Intent openMedia(Uri uri, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mimeType);
        return intent;
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
