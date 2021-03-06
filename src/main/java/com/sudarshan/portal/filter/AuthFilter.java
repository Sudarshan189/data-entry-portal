package com.sudarshan.portal.filter;

import com.sudarshan.portal.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Slf4j
public class AuthFilter extends UsernamePasswordAuthenticationFilter {

    public AuthFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/otp/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String userName = obtainUsername(request);
        String password = obtainPassword(request);
        if (userName!=null && password!=null) {
            log.info("attemptAuthentication: attempting Login for {}", userName);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
            setDetails(request, authenticationToken);
            return this.getAuthenticationManager().authenticate(authenticationToken);
        } else {
            throw new AuthenticationServiceException("Bad Credentials");
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
        if (phoneNumber!=null && !phoneNumber.isEmpty()) {
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
        log.info("Un Successful: {}", failed.getMessage());
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
