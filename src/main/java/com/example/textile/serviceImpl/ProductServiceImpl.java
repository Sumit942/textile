package com.example.textile.serviceImpl;

import com.example.textile.entity.Product;
import com.example.textile.repo.ProductRepository;
import com.example.textile.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepo;

    @Override
    public Product findByName(String name) {
        return this.productRepo.findByName(name);
    }

    @Override
    public List<Product> findByNameLike(String name) {
        return this.productRepo.findByNameLike(name);
    }
}
