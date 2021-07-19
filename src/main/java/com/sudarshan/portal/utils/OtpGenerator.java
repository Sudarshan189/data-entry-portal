package com.sudarshan.portal.utils;

import java.util.Random;

/**
 * Created By Sudarshan Shanbhag
 */
public class OtpGenerator {

    private final Random random;
    private final String format;
    private final Integer length;

    public OtpGenerator(Random random, Integer length) {
        this.random = random;
        this.length = length;
         format = "%0"+this.length+"d";
    }

    public String generateOtp() {
        double maxBound = Math.pow(10, length)-1;
        Integer number = random.nextInt((int) maxBound);
        return String.format(format, number);
    }
}
