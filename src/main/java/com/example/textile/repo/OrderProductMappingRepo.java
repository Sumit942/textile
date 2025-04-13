package com.example.textile.repo;

import com.example.textile.entity.OrderProductMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductMappingRepo extends JpaRepository<OrderProductMapping, Long> {
    List<OrderProductMapping> findByOrdersId(Long ordersId);
}
