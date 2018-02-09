package com.github.stasoption.util;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public final class DateUtils {

    public static final DateTimeFormatter DD_MMM_YYYY = DateTimeFormat.forPattern("dd MMM YYYY"); //with short month name
    public static final DateTimeFormatter DD_MMMM_YYYY = DateTimeFormat.forPattern("dd MMMM YYYY"); //with full month name
    public static final DateTimeFormatter DD_MM_YYYY = DateTimeFormat.forPattern("dd.MM.YYYY");
    public static final DateTimeFormatter H_MM = DateTimeFormat.forPattern("H:mm");

    private DateUtils() {
    }

    @NonNull
    public static String format(@NonNull DateTimeFormatter format, long time) {
        return new DateTime(time).toString(format);
    }

    @NonNull
    public static String format(@NonNull DateTimeFormatter format, @NonNull String date) {
        return new DateTime(date).toString(format);
    }

    /**
     * Returns a date that is result of addition time and days
     *
     * @param time - time for addition days
     * @param days - count of days for addition to time
     * @return result of addition time and days
     */
    public static long addDays(long time, int days) {
        return new DateTime(time)
                .plus(Period.days(days))
                .getMillis();
    }

    /**
     * Returns a date that is result of subtraction time and days
     *
     * @param time - time for addition days
     * @param days - count of days for subtraction to time
     * @return result of subtraction time and days
     */
    public static long substractDays(long time, int days) {
        return new DateTime(time)
                .minusDays(days)
                .getMillis();
    }

    public static long subtractYears(long time, int years) {
        return new DateTime(time)
                .minusYears(years)
                .getMillis();
    }

    public static long getDate(@NonNull String dateText, @NonNull DateTimeFormatter formatter) {
        return formatter
                .parseDateTime(dateText)
                .getMillis();
    }

    public static long getDate(int year, int month, int day) {
        return new DateTime(year, month, day, 12, 12).getMillis();
    }

    public static DateTime getStartOfDay(@NonNull DateTime dateTime) {
        return dateTime.withMillisOfDay(0);
    }

    public static int daysBetween(long start, long end) {
        return daysBetween(new DateTime(start), new DateTime(end));
    }

    public static int daysBetween(DateTime start, DateTime end) {
        return Days.daysBetween(start, end).getDays();
    }

    public static boolean isDifferentDates(long first, long second) {
        return !format(DD_MM_YYYY, first).equals(format(DD_MM_YYYY, second));
    }

    /**
     * Returns a time offset based on timezone in minutes.
     * The value for server end evaluates like GMT - local time in minutes.
     * It returns -180 for +03:00 (Moscow) timezone
     *
     * @return time offset in minutes
     */

    public static int getTimeOffset() {
        return Period.millis(DateTimeZone.getDefault().getStandardOffset(DateTime.now().getMillis()))
                .toStandardMinutes().getMinutes() * (-1);
    }
}
