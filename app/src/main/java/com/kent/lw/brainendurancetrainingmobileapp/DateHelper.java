package com.kent.lw.brainendurancetrainingmobileapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {


    public static String getTimeFromMili(long miili) {
        Date date = new Date();
        date.setTime(miili);
        String formattedDate = new SimpleDateFormat("HH:mm:ss").format(date);
        return formattedDate;
    }

    public static String getDateFromMili(long mili) {
        Date date = new Date();
        date.setTime(mili);
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return formattedDate;
    }

    public static String getTimeFromMs(long ms) {
        return (ms / 1000) / 60 + "m " + (ms / 1000) % 60 + "s";
    }

    public static String getDateTimeFromMili(long mili) {
        Date date = new Date();
        date.setTime(mili);
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss").format(date);
        return formattedDate;
    }
}
