package com.example.textile.serviceImpl;

import com.example.textile.command.ProductDetailCommand;
import com.example.textile.entity.*;
import com.example.textile.repo.*;
import com.example.textile.service.ProductDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

@Slf4j
@Service
public class ProductDetailsServiceImpl implements ProductDetailService {

    @Autowired private UnitRepository unitRepo;

    @Autowired private ProductDetailRepository productDetailRepo;

    @Autowired private ProductRepository productRepo;

    @Autowired private ProductExcludeRepository productExcludeRepos;

    @Autowired private CompanyRepository companyRepo;

    @Value("${spring.info.companyName}") private String companyName;

    @Autowired
    EntityManager entityManager;

    public static Company companyToExclude = null;

    @PostConstruct
    public void init() {
        log.debug("companyName: ["+companyName+"]");
        companyToExclude = companyRepo.findByName(companyName);
    }

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
    public List<ProductDetail> findAllByInvoice(Long invoice) {
        return productDetailRepo.findAllByInvoice(invoice);
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

    @Override
    public List<ProductDetail> findAllUnbilledByPartyId(Long id, List<Long> challans) {
        List<ProductExclude> excludePatterns = productExcludeRepos.findAll();

        StringBuilder sb = new StringBuilder("FROM ProductDetail pd WHERE pd.invoice IS NULL");

        if (Objects.nonNull(id) && id >= 0) {
            sb.append(" AND pd.party.id = :companyToInclude");
        } else {
            sb.append(" AND pd.party.id <> :companyToExclude");
        }

        if (Objects.nonNull(challans) && !challans.isEmpty())
            sb.append(" AND pd.chNo NOT IN (:challans)");

        if (Objects.nonNull(excludePatterns) && !excludePatterns.isEmpty()) {
            for (int i = 0; i < excludePatterns.size(); i++) {
                sb.append(" AND upper(pd.product.name) NOT LIKE upper(:exclusion").append(i).append(")");
            }
        }

        TypedQuery<ProductDetail> query = entityManager.createQuery(sb.toString(), ProductDetail.class);

        if (Objects.nonNull(id) && id >= 0) {
            query.setParameter("companyToInclude",id);
        } else {
            query.setParameter("companyToExclude", companyToExclude.getId());
        }

        if (Objects.nonNull(challans) && !challans.isEmpty())
            query.setParameter("challans",challans);

        if (Objects.nonNull(excludePatterns) && !excludePatterns.isEmpty()) {
            for (int i = 0; i < excludePatterns.size(); i++) {
                query.setParameter("exclusion"+i,excludePatterns.get(i).getPattern());
            }
        }

        return query.getResultList();
    }

    @Override
    public List<ProductDetail> findAllExcluded() {
        List<ProductExclude> excludePatterns = productExcludeRepos.findAll();

        StringBuilder sb = new StringBuilder("FROM ProductDetail pd WHERE pd.invoice IS NULL");

        if (Objects.nonNull(excludePatterns) && !excludePatterns.isEmpty()) {
            for (int i = 0; i < excludePatterns.size(); i++) {
                sb.append(" AND upper(pd.product.name) LIKE upper(:exclusion").append(i).append(")");
            }
        }

        TypedQuery<ProductDetail> query = entityManager.createQuery(sb.toString(), ProductDetail.class);

        if (Objects.nonNull(excludePatterns) && !excludePatterns.isEmpty()) {
            for (int i = 0; i < excludePatterns.size(); i++) {
                query.setParameter("exclusion"+i,excludePatterns.get(i).getPattern());
            }
        }

        return query.getResultList();
    }
}
