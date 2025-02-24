package com.example.textile.serviceimpl;

import com.example.textile.dto.CompanyYarnOrderDto;
import com.example.textile.entity.CompanyYarnOrder;
import com.example.textile.repo.CompanyYarnOrderRepository;
import com.example.textile.service.CompanyYarnOrderService;
import com.example.textile.transform.TransformationDTOToEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.textile.utility.LogUtils.*;

@Slf4j
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
        String logPrefix = "save()";
        String logSuffix = createLogSuffix("companyYarnOrderDto", companyYarnOrderDto.getId());
        log.info(createEntryLog(logPrefix));

        CompanyYarnOrder companyYarnOrder = TransformationDTOToEntity.transformCompanyYarnOrder(companyYarnOrderDto);
        if (Objects.nonNull(companyYarnOrderDto.getId()) && companyYarnOrderDto.getId().compareTo(0L) > 0) {
            CompanyYarnOrder persisted = yarnOrderRepository.findById(companyYarnOrder.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Entity Not found by Id"+ companyYarnOrderDto.getId()));
            updatePersistedCompanyYarnOrder(companyYarnOrder, persisted);
            return yarnOrderRepository.save(persisted);
        }

        log.info(createExitLog(logPrefix, logSuffix));
        return yarnOrderRepository.save(companyYarnOrder);
    }

    private void updatePersistedCompanyYarnOrder(CompanyYarnOrder companyYarnOrder, CompanyYarnOrder persisted) {
        if (persisted.getVersion().compareTo(companyYarnOrder.getVersion()) != 0) {
            throw new OptimisticLockException("Order has been already updated orderId="+ companyYarnOrder.getId() + " [ Version received="+companyYarnOrder.getVersion()+", persisted="+ persisted.getVersion() +"]");
        }

        persisted.setOrder(companyYarnOrder.getOrder());
        persisted.setOrderDt(companyYarnOrder.getOrderDt());
        persisted.setYarnInvoiceNo(companyYarnOrder.getYarnInvoiceNo());
        persisted.setRemark(companyYarnOrder.getRemark());
        persisted.setTotalAmount(companyYarnOrder.getTotalAmount());
        persisted.setTotalQuantity(companyYarnOrder.getTotalQuantity());
        persisted.setCGst(companyYarnOrder.getCGst());
        persisted.setSGst(companyYarnOrder.getSGst());
        persisted.setIGst(companyYarnOrder.getIGst());
        persisted.setYarnOrderItems(companyYarnOrder.getYarnOrderItems());
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
