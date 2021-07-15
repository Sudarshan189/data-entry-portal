package com.sudarshan.portal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
/**
 * Created By Sudarshan Shanbhag
 */
@Slf4j
@Controller
@RequestMapping({"/", "/user"})
public class UserController {

    @GetMapping({"/", "/my-assessments"})
    public String assessmentPage(Model model, HttpSession session) {
        log.debug("homePage: Homepage");
        return "home/assessments-page";
    }

    @GetMapping("/new-assessment")
    public String createAssessment(Model model, HttpSession session) {
        log.debug("homePage: Homepage");
        return "home/create-assessment";
    }
}
