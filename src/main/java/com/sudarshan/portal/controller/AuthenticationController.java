package com.sudarshan.portal.controller;

import com.sudarshan.portal.dto.PhoneDto;
import com.sudarshan.portal.exception.OtpException;
import com.sudarshan.portal.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

/**
 * Created By Sudarshan Shanbhag
 */

@Slf4j
@Controller
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping()
    public String loginPage(Model model) {
        log.debug("loginPage: Setting blank Phone Dto");
        model.addAttribute("phone", new PhoneDto());
        return "auth/login-page";
    }

    @PostMapping("/gen")
    public String genOtp(@ModelAttribute @Valid PhoneDto phone, BindingResult result, Model model) {
        log.info("genOtp: generating otp");
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            var builder = new StringBuilder();
            fieldErrors.forEach(fieldError -> builder.append(fieldError.getDefaultMessage()));
            model.addAttribute("message", builder.toString());
            return "status/error-page";
        }
        try {
            phone = authenticationService.generateOtp(phone);
            model.addAttribute("message", "OTP Sent to " + phone.getPhoneNumber());
            return "status/success-page";
        } catch (OtpException ex) {
            model.addAttribute("message", ex.getLocalizedMessage());
            return "status/error-page";
        } catch (Exception ex) {
            log.error(ex.getMessage());
            model.addAttribute("message", "Something went wrong");
            return "status/error-page";
        }
    }

    @PostMapping("/phone")
    public String submitPhone(@ModelAttribute @Valid PhoneDto phone, BindingResult bindingResult, Model model) {
        log.info("submitPhone: Phone submission {}", phone);
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            var builder = new StringBuilder();
            fieldErrors.forEach(fieldError -> builder.append(fieldError.getDefaultMessage()));
            model.addAttribute("message", builder.toString());
            return "status/error-page";
        }
        log.info("submitPhone: forwarding the request for {}", phone);
        model.addAttribute("phone", phone);
        return "forward:/login/genotp";
    }

    // forward request
    @PostMapping("/genotp")
    public String generateOtp(@ModelAttribute @Valid PhoneDto phone, Model model) {
        log.info("generateOtp: {}", phone);
        model.addAttribute("phone", phone);

        try {
            phone = authenticationService.generateOtp(phone);
            model.addAttribute("message", "OTP Sent to " + phone.getPhoneNumber());
            return "status/success-page";
        } catch (OtpException ex) {
            model.addAttribute("message", ex.getLocalizedMessage());
            return "status/error-page";
        } catch (Exception ex) {
            log.error(ex.getMessage());
            model.addAttribute("message", "Something went wrong");
            return "status/error-page";
        }
    }
}
