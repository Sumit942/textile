package com.example.textile.serviceImpl;

import com.example.textile.dto.CompanyYarnOrderDto;
import com.example.textile.entity.CompanyYarnOrder;
import com.example.textile.repo.CompanyYarnOrderRepository;
import com.example.textile.service.CompanyYarnOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CompanyYarnOrderServiceImpl implements CompanyYarnOrderService {
    private CompanyYarnOrderRepository yarnOrderRepository;
    private EntityManager entityManager;


    @Override
    public CompanyYarnOrder findById(Long id) {
        return null;//TODO: all methods code
    }

    @Override
    public List<CompanyYarnOrder> findAll() {
        return List.of();
    }

    @Override
    public CompanyYarnOrder save(CompanyYarnOrderDto companyYarnOrderDto) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Boolean existsByYarnInvoiceNo(String yarnInvoiceNo) {
        return yarnOrderRepository.existsByYarnInvoiceNo(yarnInvoiceNo);
    }

    @Override
    public Optional<CompanyYarnOrder> findByYarnInvoiceNo(String yarnInvoiceNo) {
        return yarnOrderRepository.findByYarnInvoiceNo(yarnInvoiceNo);
    }

    @Override
    public List<Long> getOrderIdByYarnInvoiceNoAndIdNot(String yarnInvoiceNo, Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<CompanyYarnOrder> from = query.from(CompanyYarnOrder.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(cb.lower(from.get("yarnInvoiceNo")), yarnInvoiceNo.toLowerCase()));

        if (Objects.nonNull(id) && id.compareTo(0L) > 0L) {
            predicates.add(cb.notEqual(from.get("order").get("id"), id));
        }

        query
                .select(from.get("order").get("id"))
                .where(cb.and(predicates.toArray(new Predicate[0])));


        return entityManager.createQuery(query).getResultList();
    }
}
