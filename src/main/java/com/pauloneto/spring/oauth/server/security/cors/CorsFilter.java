package com.pauloneto.spring.oauth.server.security.cors;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class CorsFilter extends OncePerRequestFilter {

    public static final String ACCESS_CONTROL_ALLOW_METHODS = "GET, POST, PUT, DELETE, OPTIONS";
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Content-Type, Authorization ";
    public static final String ACCESS_CONTROL_MAX_AGE = "10";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", ACCESS_CONTROL_ALLOW_METHODS);
        response.addHeader("Access-Control-Allow-Headers", ACCESS_CONTROL_ALLOW_HEADERS);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Expose-Headers", "*");
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
