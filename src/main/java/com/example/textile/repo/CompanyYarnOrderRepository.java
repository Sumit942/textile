package com.example.textile.repo;

import com.example.textile.entity.CompanyYarnOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyYarnOrderRepository extends JpaRepository<CompanyYarnOrder, Long> {
    Boolean existsByYarnInvoiceNo(String yarnInvoiceNo);

    Optional<CompanyYarnOrder> findByYarnInvoiceNoAndIdNot(String yarnInvoiceNo, Long id);

    Optional<CompanyYarnOrder> findByYarnInvoiceNo(String yarnInvoiceNo);
}
