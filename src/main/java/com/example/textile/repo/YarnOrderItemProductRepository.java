package com.example.textile.repo;

import com.example.textile.entity.CompanyYarnOrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface YarnOrderItemProductRepository extends JpaRepository<CompanyYarnOrderProduct, Long> {
}
