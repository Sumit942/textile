package com.example.textile.controller;

import com.example.textile.entity.Product;
import com.example.textile.repo.ProductRepository;
import com.example.textile.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/searchByName/{name}")
    @ResponseBody
    public List<Product> searchByName(@PathVariable("name") String name) {
        return productService.findByNameLike(name);
    }
}
