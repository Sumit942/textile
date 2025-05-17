package com.example.textile.repo;

import com.example.textile.dto.OrdersDto;
import com.example.textile.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query("SELECT COUNT(*) FROM Orders o where o.company.id=:id")
    int countByCompanyId(Long id);

    @Query("SELECT new com.example.textile.dto.OrdersDto(o.id,o.orderNo,o.company.id,o.company.name) FROM Orders o WHERE LOWER(o.orderNo) LIKE LOWER(CONCAT('%',:orderNo, '%'))")
    List<OrdersDto> getIdAndOrderNoByOrderLike(String orderNo);
}
