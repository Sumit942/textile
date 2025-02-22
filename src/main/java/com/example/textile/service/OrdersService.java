package com.example.textile.service;

import com.example.textile.dto.OrdersDto;
import com.example.textile.entity.Orders;
import com.example.textile.entity.OrdersView;

import java.util.List;

public interface OrdersService {

    List<OrdersDto> findAll();

    Orders save(OrdersDto ordersDto);

    OrdersDto findById(Long id);

    Boolean existById(Long id);

    List<OrdersView> fetchView();
}
