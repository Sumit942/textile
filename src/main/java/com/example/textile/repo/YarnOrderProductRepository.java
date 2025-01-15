package com.example.textile.repo;

import com.example.textile.entity.YarnOrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YarnOrderProductRepository extends JpaRepository<YarnOrderProduct, Long> {
}
