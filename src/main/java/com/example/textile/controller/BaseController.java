package com.example.textile.controller;

import com.example.textile.entity.User;
import com.example.textile.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequestMapping("/")
public class BaseController {

    @Autowired
    UserService userService;

    @Autowired
    MessageSource messageSource;

    public User getLoggedInUser() {
        User user = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User authenticatedUser =
                    (org.springframework.security.core.userdetails.User) principal;
            String userName = authenticatedUser.getUsername();
            if (userName != null && !userName.isEmpty()) {
                user = userService.findByUserName(userName);
            } else {
                log.error("[ERROR: USER_NOT_LOGGED_IN]");
            }
        }
        return user;
    }

}
