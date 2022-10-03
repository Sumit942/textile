package com.example.textile.repo;

public interface ProductRateRepository {

    Double getRateByCompanyAndProduct(Long companyId, Long productId);
}
