package com.example.textile.service;

import com.example.textile.entity.Product;

import java.util.List;

public interface ProductService {

    Product findByName(String name);

    List<Product> findByNameLike(String name);
}
