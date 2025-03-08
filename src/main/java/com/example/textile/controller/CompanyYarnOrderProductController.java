package com.example.textile.controller;

import com.example.textile.entity.CompanyYarnOrderProduct;
import com.example.textile.service.CompanyYarnOrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("orderProduct")
public class CompanyYarnOrderProductController {
    @Autowired
    CompanyYarnOrderProductService orderProductService;

    @GetMapping("searchBy")
    public List<CompanyYarnOrderProduct> fetchByYarnFabricDesignId(@RequestParam Long yarnFabricDesignId) {
        return orderProductService.findIdAndMachineByYarnFabricProductId(yarnFabricDesignId);
    }
}
