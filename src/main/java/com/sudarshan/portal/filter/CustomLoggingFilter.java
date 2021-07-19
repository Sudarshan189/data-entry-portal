package com.sudarshan.portal.filter;

import com.sudarshan.portal.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class CustomLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            MDC.put(Constants.REQUEST_ID_FOR_LOG, UUID.randomUUID().toString().toUpperCase().replace(Constants.HIFEN, Constants.EMPTY_STRING));
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            MDC.remove(Constants.REQUEST_ID_FOR_LOG);
        }
    }
}
