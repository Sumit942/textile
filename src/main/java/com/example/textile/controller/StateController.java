package com.example.textile.controller;

import com.example.textile.entity.State;
import com.example.textile.repo.StateRepository;
import com.example.textile.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/states")
public class StateController {

    @Autowired
    private StateService stateService;

    @GetMapping("/searchByName/{name}")
    @ResponseBody
    public List<State> searchByName(@PathVariable String name) {
        return stateService.findByNameLike(name);
    }

    @GetMapping("/findByCode/{code}")
    @ResponseBody
    public State findByCode(@PathVariable Integer code) {
        return stateService.findByCode(code);
    }
}
