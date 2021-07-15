package com.sudarshan.portal.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created By Sudarshan Shanbhag
 */
public class DateUtils {

    public static Date getExpiry(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static boolean checkOtpIfExpired(Date date) {
        return date.after(new Date());
    }
}
