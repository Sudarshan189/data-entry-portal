package com.sudarshan.portal.service.impl;

import com.sudarshan.portal.dto.PhoneDto;
import com.sudarshan.portal.domain.model.Authority;
import com.sudarshan.portal.domain.model.User;
import com.sudarshan.portal.domain.repository.UserRepository;
import com.sudarshan.portal.exception.OtpException;
import com.sudarshan.portal.service.AuthenticationService;
import com.sudarshan.portal.utils.DateUtils;
import com.sudarshan.portal.utils.OtpGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

/**
 * Created By Sudarshan Shanbhag
 */
@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${data.entry.application.admin}")
    private String adminPhoneNumber;

    @Override
    public PhoneDto generateOtp(PhoneDto phoneDto) throws OtpException {
        User user;
        String generatedOtp = OtpGenerator.generateOtp(6);
        phoneDto.setOtp(null);
        log.info("Generated OTP is: {}", generatedOtp);
        Optional<User> userFromDb = userRepository.findById(phoneDto.getPhoneNumber());
        if (userFromDb.isPresent()) {
            user = userFromDb.get();
            if (user.otpExpiryTime()!=null && DateUtils.checkOtpIfExpired(user.otpExpiryTime())) {
                log.info("generateOtp: otp not yet expired {}", user);
                throw new OtpException("Otp not yet expired. Please try after some time.");
            } else {
                log.info("generateOtp: otp expired generated new one {}", user);
                user.otpGenerated(passwordEncoder.encode(generatedOtp));
                user.otpExpiryTime(DateUtils.getExpiry(1));
                userRepository.save(user);
                return phoneDto;
            }
        } else {
                String role;
                user = new User();
                user.phoneNumber(phoneDto.getPhoneNumber());
                user.otpGenerated(passwordEncoder.encode(generatedOtp));
                user.otpExpiryTime(DateUtils.getExpiry(1));
                if (phoneDto.getPhoneNumber().equals(adminPhoneNumber)) {
                    role = "ROLE_ADMIN";
                } else {
                    role = "ROLE_USER";
                }
                Authority authority = new Authority(role);
                authority.setUser(user);
                user.addAuthority(authority);
                userRepository.save(user);
                return phoneDto;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("loadUserByUsername: {}", s);
        Optional<User> user = userRepository.findById(s);
        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setCredentialsNonExpired(foundUser.otpExpiryTime() != null && DateUtils.checkOtpIfExpired(foundUser.otpExpiryTime()));
            return foundUser;
        }
        throw new UsernameNotFoundException("User not found");
    }
}
