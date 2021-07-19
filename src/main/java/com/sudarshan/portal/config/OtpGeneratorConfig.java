package com.sudarshan.portal.config;

import com.sudarshan.portal.utils.OtpGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * Created By Sudarshan Shanbhag
 */
@Configuration
public class OtpGeneratorConfig {

    @Value("${data.entry.application.otp.length}")
    private Integer otpLength;

    @Bean
    public OtpGenerator otpGenerator() {
        return new OtpGenerator(random(), otpLength);
    }

    @Bean
    public Random random() {
        return new Random();
    }
}
