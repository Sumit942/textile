package com.example.textile.repo;

import com.example.textile.entity.YarnOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface YarnOrderItemRepo extends JpaRepository<YarnOrderItem, Long> {
    List<YarnOrderItem> findByCompanyYarnOrderOrderCompanyId(Long companyId);

    List<YarnOrderItem> findByCompanyYarnOrderOrderCompanyIdAndQtyUsedGreaterThan(Long companyId, int i);
}
