package com.github.stasoption.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;



public class ImageUtil {

    /**
     * @param picturePath The picture path
     * @param reqWidth    The required minimum width for bitmap
     * @param reqHeight   The required minimum height for bitmap
     * @return The not recycled bitmap by path with required sizes
     */
    @Nullable
    public static Bitmap getScaledBitmap(@NonNull String picturePath, int reqWidth, int reqHeight) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, reqWidth, reqHeight);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        Bitmap approximateBitmap = BitmapFactory.decodeFile(picturePath, sizeOptions);
        if (approximateBitmap == null) {
            return null;
        }
        Bitmap reqBitmap = resizeBitmap(approximateBitmap, reqWidth, reqHeight, false);
        if (!reqBitmap.equals(approximateBitmap)) {
            approximateBitmap.recycle();
        }
        return reqBitmap;
    }

    @NonNull
    public static Bitmap rotateBitmap(@NonNull Bitmap bitmap, @Nullable String imagePath) {
        int rotationAngle = getPictureOrientation(imagePath);
        if (rotationAngle != 0) {
            return rotateBitmap(bitmap, rotationAngle);
        }
        return bitmap;
    }

    @Nullable
    public static Bitmap getScaledBitmapWithAuthority(@NonNull Context context, @NonNull Uri uri, int size) {
        Bitmap bitmap = getBitmapWithAuthority(context, uri);
        Bitmap resultBitmap = bitmap;
        if (bitmap != null) {
            resultBitmap = resizeBitmap(bitmap, size, size, false);
            bitmap.recycle();
        }
        return resultBitmap;
    }


    @NonNull
    public static Bitmap resizeBitmap(@NonNull Bitmap source, int reqWidth, int reqHeight, boolean isRecycle) {
        int maxSize = Math.max(source.getWidth(), source.getHeight());
        int minReqSize = Math.min(reqHeight, reqWidth);
        double scale = (double) minReqSize / (double) maxSize;
        int width = (int) (source.getWidth() * scale);
        int height = (int) (source.getHeight() * scale);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(source, width, height, true);
        if (isRecycle && source.equals(resizedBitmap)) {
            source.recycle();
        }
        return resizedBitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    private static int getPictureOrientation(@Nullable String picturePath) {
        int orientation = 0;
        if (picturePath == null) {
            return orientation;
        }

        try {
            ExifInterface exifInterface = new ExifInterface(picturePath);
            int exifOrientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            orientation = getOrientationInDegree(exifOrientation);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return orientation;
    }

    @NonNull
    public static Bitmap rotateBitmap(@NonNull Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap rotatedBitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        source.recycle();
        return rotatedBitmap;
    }


    @Nullable
    private static Bitmap getBitmapWithAuthority(@NonNull Context context, @NonNull Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static int getOrientationInDegree(int exifOrientation) {
        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            default:
                return 0;
        }
    }

    public static Bitmap resizeByOneSide(Bitmap source, int maxSizeValue) {
        int targetWidth, targetHeight;
        double aspectRatio;

        if (source.getWidth() > source.getHeight()) {
            targetWidth = maxSizeValue;
            aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            targetHeight = (int) (targetWidth * aspectRatio);
        } else {
            targetHeight = maxSizeValue;
            aspectRatio = (double) source.getWidth() / (double) source.getHeight();
            targetWidth = (int) (targetHeight * aspectRatio);
        }

        Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
        if (result != source) {
            source.recycle();
        }
        return result;
    }


    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
