package com.example.textile.controller;

import com.example.textile.entity.Company;
import com.example.textile.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping("/searchByName/{name}")
    @ResponseBody
    public List<Company> searchByName(@PathVariable("name") String name) {
        return companyService.findByNameLike(name);
    }
}
