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
        log.debug("loginPage: Setting blank Phone Dto");
        model.addAttribute("phone", new PhoneDto());
        return "auth/login-page";
    }

    @PostMapping("/login/failed")
    public String handleFailedLogin(Model model, HttpServletRequest request, HttpServletResponse response) {
        log.info("handleFailedLogin: Login failed");
        model.addAttribute("phone", new PhoneDto(request.getParameter("phoneNumber")));
        return "auth/login-page";
    }

    @PostMapping("/login/phone")
    public String submitPhone(@ModelAttribute @Valid PhoneDto phone, BindingResult bindingResult, Model model) {
        log.info("submitPhone: Phone submission {}", phone);
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            var builder = new StringBuilder();
            fieldErrors.forEach(fieldError -> builder.append(fieldError.getDefaultMessage()));
            model.addAttribute("failed", builder.toString());
            model.addAttribute("phone", new PhoneDto());
            return "auth/login-page";
        }
        log.info("submitPhone: forwarding the request for {}", phone);
        model.addAttribute("phone", phone);
        return "forward:/genotp";
    }

    // forward request
    @PostMapping("/genotp")
    public String generateOtp(@ModelAttribute @Valid PhoneDto phone, Model model) {
        log.info("generateOtp forwarded: {}", phone);
        model.addAttribute("phone", phone);

        try {
            phone = authenticationService.generateOtp(phone);
            model.addAttribute("success", "OTP Sent to " + phone.getPhoneNumber());
            model.addAttribute("phone", phone);
            return "auth/login-page";
        } catch (OtpException ex) {
            model.addAttribute("failed", ex.getLocalizedMessage());
            model.addAttribute("phone", new PhoneDto());
            return "auth/login-page";
        } catch (Exception ex) {
            log.error(ex.getMessage());
            model.addAttribute("failed", "Something went wrong. Please try again.");
            model.addAttribute("phone", new PhoneDto());
            return "auth/login-page";
        }
    }
}
