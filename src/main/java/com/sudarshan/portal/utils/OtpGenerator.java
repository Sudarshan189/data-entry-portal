package com.sudarshan.portal.utils;

import java.util.Random;

/**
 * Created By Sudarshan Shanbhag
 */
public class OtpGenerator {

    private OtpGenerator() {
    }

    public static final Random random = new Random();

    public static String generateOtp(Integer length) {
        double maxBound = Math.pow(10, length)-1;
        Integer number = random.nextInt((int) maxBound);
        return String.format("%06d", number);
    }
}
