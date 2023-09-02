package com.example.textile.service;

import com.example.textile.entity.Product;

import java.util.List;

public interface ProductService {

    Product findByName(String name);

    List<Product> findByNameLike(String name);

    List<Product> findByNameAndLimit(String name, int limit);

    Product findById(Long id);

    Product save(Product product);
}
