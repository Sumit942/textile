package com.example.textile.repo;

import com.example.textile.entity.OrdersView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersViewRepo extends JpaRepository<OrdersView, Long> {
}
