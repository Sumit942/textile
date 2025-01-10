package com.example.textile.controller;

import com.example.textile.dto.CompanyDto;
import com.example.textile.entity.Company;
import com.example.textile.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @ResponseBody
    @PostMapping("/save")
    public ResponseEntity<CompanyDto> save(@RequestBody CompanyDto companyDto) {
        CompanyDto saved = companyService.save(companyDto);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
