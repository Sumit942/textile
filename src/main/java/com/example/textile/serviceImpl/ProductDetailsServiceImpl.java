package com.example.textile.serviceImpl;

import com.example.textile.entity.Product;
import com.example.textile.entity.ProductDetail;
import com.example.textile.entity.Unit;
import com.example.textile.repo.ProductDetailRepository;
import com.example.textile.repo.ProductRepository;
import com.example.textile.repo.UnitRepository;
import com.example.textile.service.ProductDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductDetailsServiceImpl implements ProductDetailService {

    @Autowired private UnitRepository unitRepo;

    @Autowired private ProductDetailRepository productDetailRepo;

    @Autowired private ProductRepository productRepo;

    @Override
    public List<Unit> findAllUnit() {
        return unitRepo.findAll();
    }

    @Override
    public List<ProductDetail> saveAll(List<ProductDetail> productDetails) {
        String logPrefix = "saveAll() |";
        log.info("{} Entry", logPrefix);
        productDetails.stream()
                .filter(e -> e.getProduct().getId() == null)
                .forEach(e -> {
                    Product savedPrdt = productRepo.findByNameIgnoreCase(e.getProduct().getName());
                    if (savedPrdt != null) {
                        e.getProduct().setId(savedPrdt.getId());
                    } else {
                        productRepo.save(e.getProduct());
                        log.info("{} saving product{} ", logPrefix,e.getProduct());
                    }
                });

        log.info("{} Exit", logPrefix);
        return productDetailRepo.saveAll(productDetails);
    }

    @Override
    public List<ProductDetail> findByChNo(String chNo) {
        return productDetailRepo.findByChNo(chNo);
    }
}
