package com.example.textile.repo;

import com.example.textile.entity.YarnOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface YarnOrderItemRepo extends JpaRepository<YarnOrderItem, Long> {
    List<YarnOrderItem> findByCompanyYarnOrderOrderCompanyId(Long companyId);

    List<YarnOrderItem> findByCompanyYarnOrderOrderCompanyIdAndQtyLeftGreaterThan(Long companyId, BigDecimal quantity);

    List<YarnOrderItem> findByCompanyYarnOrderOrderCompanyIdAndYarnId(Long companyId, Long yarnId);

    List<YarnOrderItem> findByCompanyYarnOrderOrderCompanyIdAndYarnIdAndQtyLeftGreaterThan(Long companyId, Long yarnId, BigDecimal quantity);
}
