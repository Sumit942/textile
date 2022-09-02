package com.example.textile.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController extends BaseController {

    @GetMapping("/login")
    public String loginPage() {

        return "login";
    }

}
