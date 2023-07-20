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

import javax.persistence.EntityManager;
import java.util.*;

@Slf4j
@Service
public class ProductDetailsServiceImpl implements ProductDetailService {

    @Autowired private UnitRepository unitRepo;

    @Autowired private ProductDetailRepository productDetailRepo;

    @Autowired private ProductRepository productRepo;

    @Autowired
    EntityManager entityManager;

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
                        if (!savedPrdt.getActive()) {
                            savedPrdt.setActive(true);
                            productRepo.save(savedPrdt);
                        }
                        e.getProduct().setId(savedPrdt.getId());
                    } else {
                        if (!e.getProduct().getActive())
                            e.getProduct().setActive(true);
                        productRepo.save(e.getProduct());
                        log.info("{} saving product{} ", logPrefix,e.getProduct());
                    }
                });
        productDetails.stream().filter(e -> e.getProduct().getId() != null)
                    .forEach(e -> {
                        if (!e.getProduct().getActive()) {
                            e.getProduct().setActive(true);
                            productRepo.save(e.getProduct());
                            log.info("{} updating Product [Active =true; id ={}]", logPrefix, e.getProduct().getId());
                        }
                    });

        log.info("{} Exit", logPrefix);
        return productDetailRepo.saveAll(productDetails);
    }

    @Override
    public List<ProductDetail> findByChNo(String chNo) {
        return productDetailRepo.findByChNo(chNo);
    }

    @Override
    public List<String> findAllChNo() {
        return productDetailRepo.findAllChNo();
    }

    @Override
    public List<ProductDetail> findByPartyIdAndInvoiceId(Long partyId, Long invoiceId) {
        return productDetailRepo.findByPartyIdAndInvoiceId(partyId, invoiceId);
    }

}
