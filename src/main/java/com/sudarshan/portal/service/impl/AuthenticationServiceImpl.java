package com.sudarshan.portal.service.impl;

import com.sudarshan.portal.dto.PhoneDto;
import com.sudarshan.portal.model.Authority;
import com.sudarshan.portal.model.User;
import com.sudarshan.portal.repository.UserRepository;
import com.sudarshan.portal.service.AuthenticationService;
import com.sudarshan.portal.utils.OtpGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Boolean generateOtp(PhoneDto phoneDto) {
        String generatedOtp = OtpGenerator.generateOtp(6);
        log.info("Generated OTP is: {}", generatedOtp);
        User user = new User();
        user.phoneNumber(phoneDto.getPhoneNumber());
        user.otpGenerated(passwordEncoder.encode(generatedOtp));
        user.addAuthority(new Authority("ROLE_USER"));
        userRepository.saveUser(user);
        // call mail or mobile service
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("loadUserByUsername: {}", s);
        User user = userRepository.getUserByPhone(s);
        if (user!=null) {
            log.info("loadUserByUsername: {}", user);
            return user;
        }
        throw new UsernameNotFoundException("Please enter otp to login");
    }
}
