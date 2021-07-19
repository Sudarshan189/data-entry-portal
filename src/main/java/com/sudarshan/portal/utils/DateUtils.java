package com.sudarshan.portal.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created By Sudarshan Shanbhag
 */
public class DateUtils {

    private DateUtils() {

    }

    public static Date getExpiry(int minutes) {
        var calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static boolean checkOtpIfExpired(Date date) {
        return date.after(new Date());
    }
}
