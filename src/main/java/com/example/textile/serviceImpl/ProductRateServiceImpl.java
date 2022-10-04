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

    private static final Map<Long, Map<Long, Double>> companyProductRateCacheMap = new HashMap<>();

    @Override
    public Double getRateByCompanyAndProduct(Long companyId, Long productId) {
        return getRateByCompanyAndProductFromCache(companyId, productId);
    }

    public Double getRateByCompanyAndProductFromCache(Long companyId, Long productId) {
        Map<Long, Double> productRateMap = companyProductRateCacheMap.get(companyId);
        if (productRateMap != null) {
            Double productRateByCompany = productRateMap.get(productId);
            if (productRateByCompany != null) {
                return productRateByCompany;
            } else {
                Double rate = productRateRepo.getRateByCompanyAndProduct(companyId, productId);
                productRateMap.put(productId, rate);
                return rate;
            }
        } else {
            productRateMap = new HashMap<>();
            Double rate = productRateRepo.getRateByCompanyAndProduct(companyId, productId);
            productRateMap.put(productId, rate);
            companyProductRateCacheMap.put(companyId,productRateMap);
            return rate;
        }
    }
}
