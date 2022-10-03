package com.example.textile.repoImpl;

import com.example.textile.repo.ProductRateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Slf4j
@Repository
public class ProductRateRepositoryImpl implements ProductRateRepository {

    @Autowired
    EntityManager entityManager;

    @Override
    public Double getRateByCompanyAndProduct(Long companyId, Long productId) {

        String logPrefix = "getRateByCompanyAndProduct()";
        log.info("{} | Entry",logPrefix);
        Double maxRate = 0.0;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT MAX(pd.rate) FROM ProductDetail pd");
        sb.append(" WHERE pd.product.id IN (:productId)");

        if (companyId != null && companyId != 0) {
            sb.append(" AND pd.invoice.billToParty.id IN (:companyId)");
        }

        Query query = entityManager.createQuery(sb.toString());
        if (companyId != null && companyId != 0) {
            query.setParameter("companyId", companyId);
        }
        query.setParameter("productId",productId);

        List resultList = query.getResultList();

        if (resultList != null && !resultList.isEmpty()) {
            maxRate = (Double) resultList.get(0);
            log.info("{} [company={};product={};maxRate={}]",logPrefix,companyId,productId,maxRate);
        }
        log.info("{} | Exit",logPrefix);
        return maxRate;
    }
}
