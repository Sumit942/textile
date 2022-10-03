package com.example.textile.serviceImpl;

import com.example.textile.repo.ProductRateRepository;
import com.example.textile.service.ProductRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductRateServiceImpl implements ProductRateService {

    @Autowired
    ProductRateRepository productRateRepo;

    Map<Long, Map<Long, String>> companyProductRateCacheMap = new HashMap<>();

    @Override
    public String getRateByCompanyAndProduct(Long companyId, Long productId) {
        return getRateByCompanyAndProductFromCache(companyId, productId);
    }

    public String getRateByCompanyAndProductFromCache(Long companyId, Long productId) {
        Map<Long, String> productRateMap = companyProductRateCacheMap.get(companyId);
        if (productRateMap != null) {
            String productRateByCompany = productRateMap.get(productId);
            if (productRateByCompany != null) {
                return productRateByCompany;
            } else {
                String rate = productRateRepo.getRateByCompanyAndProduct(companyId, productId);
                productRateMap.put(productId, rate);
                return rate;
            }
        } else {
            productRateMap = new HashMap<>();
            String rate = productRateRepo.getRateByCompanyAndProduct(companyId, productId);
            productRateMap.put(productId, rate);
            return rate;
        }
    }
}
