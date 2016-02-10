package com.kennedy.peter.bloquery.helpers;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocalizedDateAndTime {
    public static String epochToDate(String epoch) {
        String s = new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.getDefault(),
                "ddMMyyyy hhmmss")).format(new Date(Long.parseLong(epoch)));
        return s;
    }
}
