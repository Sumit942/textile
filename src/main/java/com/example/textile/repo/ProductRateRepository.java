package com.example.textile.repo;

public interface ProductRateRepository {

    String getRateByCompanyAndProduct(Long companyId, Long productId);
}
