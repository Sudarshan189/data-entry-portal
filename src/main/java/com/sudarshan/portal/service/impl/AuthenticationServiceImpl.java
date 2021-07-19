package com.sudarshan.portal.service.impl;

import com.sudarshan.portal.dto.PhoneDto;
import com.sudarshan.portal.domain.model.Authority;
import com.sudarshan.portal.domain.model.User;
import com.sudarshan.portal.domain.repository.UserRepository;
import com.sudarshan.portal.exception.OtpException;
import com.sudarshan.portal.service.AuthenticationService;
import com.sudarshan.portal.utils.Constants;
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

    @Value("${data.entry.application.otp.expiry}")
    private Integer otpExpiryTime;

    @Autowired
    private OtpGenerator otpGenerator;

    @Override
    public PhoneDto generateOtp(PhoneDto phoneDto) throws OtpException {
        User user;
        String generatedOtp = otpGenerator.generateOtp();
        phoneDto.setOtp(null);
        Optional<User> userFromDb = userRepository.findById(phoneDto.getPhoneNumber());
        if (userFromDb.isPresent()) {
            user = userFromDb.get();
            if (user.otpExpiryTime()!=null && DateUtils.checkOtpIfExpired(user.otpExpiryTime())) {
                log.info("OTP not yet expired for {}", user);
                throw new OtpException("Otp not yet expired. Please try after some time.");
            } else {
                log.debug("Generated OTP is: {}", generatedOtp);
                log.info("Generating new OTP as OTP already expired for {}", user);
                user.otpGenerated(passwordEncoder.encode(generatedOtp));
                user.otpExpiryTime(DateUtils.getExpiry(otpExpiryTime));
                userRepository.save(user);
                return phoneDto;
            }
        } else {
                String role;
                log.debug("Generated OTP is: {}", generatedOtp);
                log.info("Generating new OTP for {}", phoneDto);
                user = new User();
                user.phoneNumber(phoneDto.getPhoneNumber());
                user.otpGenerated(passwordEncoder.encode(generatedOtp));
                user.otpExpiryTime(DateUtils.getExpiry(otpExpiryTime));
                if (phoneDto.getPhoneNumber().equals(adminPhoneNumber)) {
                    role = Constants.ROLE_ADMIN;
                } else {
                    role = Constants.ROLE_USER;
                }
                var authority = new Authority(role);
                authority.setUser(user);
                user.addAuthority(authority);
                userRepository.save(user);
                return phoneDto;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(s);
        if (user.isPresent()) {
            var foundUser = user.get();
            foundUser.setCredentialsNonExpired(foundUser.otpExpiryTime() != null && DateUtils.checkOtpIfExpired(foundUser.otpExpiryTime()));
            log.debug("User found {}", foundUser);
            return foundUser;
        }
        log.info("User not found {}", s);
        throw new UsernameNotFoundException("User not found");
    }
}
