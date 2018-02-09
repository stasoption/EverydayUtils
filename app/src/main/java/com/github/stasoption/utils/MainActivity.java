package com.github.stasoption.utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.stasoption.util.ActionBarUtils;
import com.github.stasoption.util.AndroidUtil;
import com.github.stasoption.util.DateUtils;
import com.github.stasoption.util.DeviceUtils;
import com.github.stasoption.util.Validator;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*ActionBar utils*/
        int actionBarHeight = ActionBarUtils.getActionBarHeight(this);
        Log.d("actionBarHeight", String.valueOf(actionBarHeight));

        ActionBarUtils.setStatusBarColor(this, R.color.statusBarColor);

        /*Android utils*/
        boolean isProviderEnabled = AndroidUtil.isProviderEnabled(this);
        Log.d("isProviderEnabled", String.valueOf(isProviderEnabled));

        /*Date utils*/
        String currentDate = DateUtils.format(DateUtils.DD_MM_YYYY, System.currentTimeMillis());
        Log.d("currentDate", String.valueOf(currentDate));

        long plusTwoDaysDate = DateUtils.addDays(System.currentTimeMillis(), 2);
        Log.d("plusTwoDaysDate", String.valueOf(DateUtils.format(DateUtils.DD_MM_YYYY, plusTwoDaysDate)));

        long subtractTwoDaysDate = DateUtils.subtractDays(System.currentTimeMillis(), 2);
        Log.d("subtractTwoDaysDate", String.valueOf(DateUtils.format(DateUtils.DD_MM_YYYY, subtractTwoDaysDate)));

        long subtractTwoYearsDate = DateUtils.subtractYears(System.currentTimeMillis(), 2);
        Log.d("subtractTwoYearsDate", String.valueOf(DateUtils.format(DateUtils.DD_MM_YYYY, subtractTwoYearsDate)));

        long getDateFromYearMonthDay = DateUtils.getDate(1985, 12, 11);
        Log.d("getDateFromYearMonthDay", String.valueOf(getDateFromYearMonthDay));

        long today = System.currentTimeMillis();
        long atWeek = DateUtils.addDays(System.currentTimeMillis(), 7);
        long daysBetween = DateUtils.daysBetween(today, atWeek);
        Log.d("daysBetween", String.valueOf(daysBetween));

        boolean isDifferentDates = DateUtils.isDifferentDates(today, atWeek);
        Log.d("isDifferentDates", String.valueOf(isDifferentDates));

        /*Device utils*/
        String deviceId = DeviceUtils.getDeviceId(this);
        Log.d("deviceId", String.valueOf(deviceId));

        /*Validation*/
        boolean isValidEmailTrue = Validator.validateEmail("averin.developer@gmail.com");
        boolean isValidEmailFalse = Validator.validateEmail("averin .developer@gmail<com");
        Log.d("isValidEmailTrue", String.valueOf(isValidEmailTrue));
        Log.d("isValidEmailFalse", String.valueOf(isValidEmailFalse));

        boolean isValidPhoneTrue = Validator.validateRusPhone("79112223344");
        boolean isValidPhoneFalse = Validator.validateRusPhone("7911qwe334");
        Log.e("isValidPhoneTrue", String.valueOf(isValidPhoneTrue));
        Log.e("isValidPhoneFalse", String.valueOf(isValidPhoneFalse));
    }
}
