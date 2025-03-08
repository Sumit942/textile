package com.example.textile.service;

import com.example.textile.entity.CompanyYarnOrderProduct;

import java.util.List;

public interface CompanyYarnOrderProductService {
    List<CompanyYarnOrderProduct> findIdAndMachineByYarnFabricProductId(Long yarnFabricProductId);
}
