package com.example.textile.serviceImpl;

import com.example.textile.command.ProductDetailCommand;
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
import javax.persistence.Query;
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
    public List<ProductDetail> findByChNo(Long chNo) {
        return productDetailRepo.findByChNo(chNo);
    }

    @Override
    public List<Long> findAllChNo() {
        return productDetailRepo.findAllChNo();
    }

    @Override
    public List<ProductDetail> findByPartyIdAndInvoiceId(Long partyId, Long invoiceId) {
        return productDetailRepo.findByPartyIdAndInvoiceId(partyId, invoiceId);
    }

    @Override
    public List<ProductDetail> findByPartyIdAndProductName_Not_Like(Long partyId, String productName) {
        return productDetailRepo.findByPartyIdAndProductName_Not_Like(partyId, productName);
    }

    @Override
    public List<Long> findAllChNoAndInvoice_IsNull() {
        return productDetailRepo.findAllChNoAndInvoice_IsNull();
    }

    @Override
    public List<ProductDetail> findAllByInvoice(Long invoice) {
        return productDetailRepo.findAllByInvoice(invoice);
    }

    @Override
    public List<ProductDetail> findAllByInvoice_Is_Null_And_productName_EndsWith(String prodName) {
        return productDetailRepo.findAllByInvoice_Is_Null_And_productName_EndsWith(prodName);
    }

    @Override
    public List<ProductDetail> findAllByInvoice_Is_Null_And_productName_Not_EndsWith(String prodName) {
        return productDetailRepo.findAllByInvoice_Is_Null_And_productName_Not_EndsWith(prodName);
    }

    @Override
    public List<ProductDetail> challanReport(ProductDetailCommand command) {

        StringBuilder sb = new StringBuilder("SELECT pd FROM ProductDetail pd WHERE pd.id >0");
        if (Objects.nonNull(command.getChallanNos()) && !command.getChallanNos().isEmpty()) {
            sb.append(" AND pd.chNo IN (:chNo)");
        }
        if (Objects.nonNull(command.getCompany()) && Objects.nonNull(command.getCompany().getId())) {
            sb.append(" AND pd.party.id= :partyId");
        }
        if (Objects.nonNull(command.getProduct())) {
            if (Objects.nonNull(command.getProduct().getId())) {
                sb.append(" AND pd.product.id= :productId");
            } else if (Objects.nonNull(command.getProduct().getName()) && !command.getProduct().getName().isEmpty()) {
                sb.append(" AND pd.product.name LIKE :productName");
            }
        }

        Query query = entityManager.createQuery(sb.toString());

        if (Objects.nonNull(command.getChallanNos()) && !command.getChallanNos().isEmpty()) {
            query.setParameter("chNo",command.getChallanNos());
        }
        if (Objects.nonNull(command.getCompany()) && Objects.nonNull(command.getCompany().getId())) {
            query.setParameter("partyId",command.getCompany().getId());
        }
        if (Objects.nonNull(command.getProduct())) {
            if (Objects.nonNull(command.getProduct().getId())) {
                query.setParameter("productId",command.getProduct().getId());
            } else if (Objects.nonNull(command.getProduct().getName()) && !command.getProduct().getName().isEmpty()) {
                query.setParameter("productName",command.getProduct().getName());
            }
        }
        query.setMaxResults(10);

        return (List<ProductDetail>) query.getResultList();
    }
}
