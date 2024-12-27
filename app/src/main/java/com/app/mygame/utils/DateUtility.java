package com.app.mygame.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtility {

    // Default date format
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Formats a Date object into a string using the default date format.
     *
     * @param date the Date object to be formatted
     * @return the formatted date string
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return ""; // Return an empty string if the date is null
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
        return dateFormat.format(date);
    }

    /**
     * Formats a Date object into a string using a custom date format.
     *
     * @param date the Date object to be formatted
     * @param format the date format pattern
     * @return the formatted date string
     */
    public static String formatDate(Date date, String format) {
        if (date == null) {
            return ""; // Return an empty string if the date is null
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }
}
