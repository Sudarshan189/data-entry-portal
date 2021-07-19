package com.sudarshan.portal.controller;

import com.sudarshan.portal.dto.PhoneDto;
import com.sudarshan.portal.exception.OtpException;
import com.sudarshan.portal.service.AuthenticationService;
import com.sudarshan.portal.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Created By Sudarshan Shanbhag
 */

@Slf4j
@Controller
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        log.debug("Setting up empty Phone Object");
        model.addAttribute(Constants.PHONE_OBJ, new PhoneDto());
        return Constants.LOGIN_PAGE;
    }

    @PostMapping("/login/failed")
    public String handleFailedLogin(Model model, HttpServletRequest request, HttpServletResponse response) {
        String phoneNumber = request.getParameter(Constants.PHONE_NUMBER);
        log.info("Failed to Authenticate {}", phoneNumber);
        model.addAttribute(Constants.PHONE_OBJ, new PhoneDto(phoneNumber));
        return Constants.LOGIN_PAGE;
    }

    @PostMapping("/login/phone")
    public String submitPhone(@ModelAttribute @Valid PhoneDto phone, BindingResult bindingResult, Model model) {
        log.debug("Initiating Otp generation {}", phone);
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            var builder = new StringBuilder();
            fieldErrors.forEach(fieldError -> builder.append(fieldError.getDefaultMessage()));
            model.addAttribute(Constants.FAILED_OBJ, builder.toString());
            model.addAttribute(Constants.PHONE_OBJ, new PhoneDto());
            log.info("Something issues with input {}", builder.toString());
            return Constants.LOGIN_PAGE;
        }
        model.addAttribute(Constants.PHONE_OBJ, phone);
        return "forward:/genotp";
    }

    // forward request
    @PostMapping("/genotp")
    public String generateOtp(@ModelAttribute @Valid PhoneDto phone, Model model) {
        log.debug("Initiating OTP generation {}", phone);
        try {
            phone = authenticationService.generateOtp(phone);
            model.addAttribute(Constants.SUCCESS_OBJ, "OTP Sent to " + phone.getPhoneNumber());
            log.debug("OTP generated for {}", phone);
            model.addAttribute(Constants.PHONE_OBJ, phone);
        } catch (OtpException ex) {
            model.addAttribute(Constants.FAILED_OBJ, ex.getLocalizedMessage());
            model.addAttribute(Constants.PHONE_OBJ, new PhoneDto());
            log.info("Error encountered {}", ex.getLocalizedMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            model.addAttribute(Constants.FAILED_OBJ, "Something went wrong. Please try again.");
            model.addAttribute(Constants.PHONE_OBJ, new PhoneDto());
            log.info("Error encountered {}", ex.getLocalizedMessage());
        }
        return Constants.LOGIN_PAGE;
    }
}
