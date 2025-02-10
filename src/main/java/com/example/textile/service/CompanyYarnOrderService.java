package com.example.textile.service;

import com.example.textile.dto.CompanyYarnOrderDto;
import com.example.textile.entity.CompanyYarnOrder;

import java.util.List;
import java.util.Optional;

public interface CompanyYarnOrderService {

    CompanyYarnOrder findById(Long id);

    List<CompanyYarnOrder> findAll();

    CompanyYarnOrder save(CompanyYarnOrderDto companyYarnOrderDto);

    void deleteById(Long id);

    Boolean existsByYarnInvoiceNo(String yarnInvoiceNo);

    Optional<CompanyYarnOrder> findByYarnInvoiceNo(String yarnInvoiceNo);

    List<Long> getOrderIdByYarnInvoiceNoAndIdNot(String yarnInvoiceNo, Long id);
}
