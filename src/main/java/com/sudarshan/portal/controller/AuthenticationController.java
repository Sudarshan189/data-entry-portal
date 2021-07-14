package com.sudarshan.portal.controller;

import com.sudarshan.portal.dto.PhoneDto;
import com.sudarshan.portal.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Slf4j
@Controller
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        log.debug("loginPage: Setting blank Phone Dto");
        model.addAttribute("phone", new PhoneDto());
        return "auth/login";
    }

    @PostMapping("/login/gen")
    public String genOtp(@ModelAttribute PhoneDto phone) {
        log.info("genOtp: generating otp");
        authenticationService.generateOtp(phone);
        return "status/success-page";
    }
}
