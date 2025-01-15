package com.example.textile.service;

import com.example.textile.dto.OrdersDto;

import java.util.List;

public interface OrdersService {

    List<OrdersDto> findAll();

    OrdersDto saveOrUpdate(OrdersDto ordersDto);

    OrdersDto findById(Long id);
}
