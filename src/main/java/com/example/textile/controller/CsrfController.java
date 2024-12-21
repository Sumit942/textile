package com.example.textile.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class CsrfController {

    @GetMapping("/csrf")
    public CsrfToken csrfToken(HttpServletRequest request) {
        log.info("csrfToken() Entry");
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }
}
