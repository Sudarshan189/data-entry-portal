package com.sudarshan.portal.filter;

import com.sudarshan.portal.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.ui.Model;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created By Sudarshan Shanbhag
 */
@Slf4j
public class CustomUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private Model model;

    public CustomUsernamePasswordAuthFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/otp/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String userName = obtainUsername(request);
        String password = obtainPassword(request);
        if (userName!=null && password!=null) {
            log.info("attemptAuthentication: attempting Login for {}", userName);
            var authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
            setDetails(request, authenticationToken);
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } else {
            log.info("attemptAuthentication: Bad Creds {} {}",userName,password);
            throw new AuthenticationServiceException("Invalid OTP or PhoneNumber submitted");
        }
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        String otp = request.getParameter("otp");
        if (otp!=null && !otp.isEmpty()) {
            return otp;
        }
        return null;
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String phoneNumber = request.getParameter("phoneNumber");
        if (phoneNumber != null && phoneNumber.length() == 10) {
            return phoneNumber;
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        request.getSession().setAttribute("username", user.getUsername());
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("Un Successful: {}", failed.getLocalizedMessage());
        request.setAttribute("failed", failed.getLocalizedMessage());
        request.getRequestDispatcher("/login/failed").forward(request, response);
    }
}
