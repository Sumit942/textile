package com.example.textile.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReactController {

    @RequestMapping("/react/**")
    public String redirectToReact() {
        // Forward to React's index.html located in the static/react folder
        return "forward:/static/react/index.html";
    }
}

