package com.example.textile.repo;

import com.example.textile.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByNameIgnoreCase(String name);

    @Query("select p from Product p where lower(p.name) like concat('%',lower(:name),'%')")
    List<Product> findByNameLike(String name);
}
