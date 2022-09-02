package com.example.textile.controller;

import com.example.textile.entity.InvoiceView;
import com.example.textile.response.DataTableReponse;
import com.example.textile.service.InvoiceViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//@Controller
//@RequestMapping("/invoiceViews")
public class InvoiceViewController {

    @Autowired
    InvoiceViewService viewService;

    @ResponseBody
    @GetMapping
    public List<InvoiceView> findAll() {
        return viewService.findAll();
    }
}
