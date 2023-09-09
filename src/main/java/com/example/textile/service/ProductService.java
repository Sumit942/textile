package com.example.textile.service;

import com.example.textile.entity.Product;
import com.example.textile.entity.Yarn;
import com.example.textile.entity.YarnDesign;

import java.util.List;

public interface ProductService {

    Product findByName(String name);

    List<Product> findByNameLike(String name);

    List<Product> findByNameAndLimit(String name, int limit, Boolean active);

    Product findById(Long id);

    Product save(Product product);

    List<Yarn> findAllYarn();

    List<YarnDesign> findAllYarnDesign();
}
