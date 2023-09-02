package com.example.textile.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RoleBasedAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String logPrefix = "onAuthenticationSuccess() ";
        log.debug("{}Entry",logPrefix);
        if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_DBA"))) {
            log.debug("{} admin roles allowed",logPrefix);
            response.sendRedirect("invoices");
        } else if (authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
            log.debug("{} user roles allowed",logPrefix);
            response.sendRedirect("productDetail");
        } else {
            log.debug("{} no roles allowed",logPrefix);
            authentication.setAuthenticated(false);
            response.sendRedirect("/login");
        }
    }
}
