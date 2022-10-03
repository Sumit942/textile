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
    public String getRateByCompanyAndProduct(Long companyId, Long productId) {

        String logPrefix = "getRateByCompanyAndProduct()";
        log.info("{} | Entry",logPrefix);
        String maxRate = "0";
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT max(pd.rate) FROM invoice i JOIN company c ON i.bill_to_party_id=c.id");
        sb.append(" JOIN product_detail pd ON i.id=pd.invoice_id JOIN product p ON pd.product_id=p.id");
        sb.append(" WHERE p.id IN (:productId)");

        if (companyId != null && companyId != 0) {
            sb.append(" and c.id IN (:companyId)");
        }

        Query query = entityManager.createQuery(sb.toString());
        if (companyId != null && companyId != 0) {
            query.setParameter("companyId", companyId);
        }
        query.setParameter("productId",productId);

        List resultList = query.getResultList();

        if (resultList != null && !resultList.isEmpty()) {
            maxRate = (String) resultList.get(0);
            log.info("{} [company={};product={};maxRate={}]",logPrefix,companyId,productId,maxRate);
        }
        log.info("{} | Exit",logPrefix);
        return maxRate;
    }
}
