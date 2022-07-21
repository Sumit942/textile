package com.example.textile.controller;

import com.example.textile.entity.Company;
import com.example.textile.entity.Invoice;
import com.example.textile.entity.State;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/getFormat")
public class BlankFormatController {

    @GetMapping("/invoice")
    public Invoice showNewInvoice() {
        return new Invoice();
    }
    @GetMapping("/company")
    public Company showNewCompany() {
        return new Company();
    }
    @GetMapping("/state")
    public State showState() {
        return new State();
    }
}
