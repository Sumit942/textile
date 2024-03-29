package com.example.textile.controller;

import com.example.textile.entity.Product;
import com.example.textile.repo.ProductRepository;
import com.example.textile.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/searchByName")
    @ResponseBody
    public List<Product> searchByName(@RequestParam("name") String name) {
        return productService.findByNameLike(name);
    }
}
