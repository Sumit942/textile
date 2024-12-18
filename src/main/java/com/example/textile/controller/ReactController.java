package com.example.textile.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class ReactController {

    @RequestMapping("/react/**")
    public String redirectToReact() {
        log.info("redirectToReact() Entry");
        // Forward to React's index.html located in the static/react folder
        return "forward:/static/react/index.html";
    }
}

