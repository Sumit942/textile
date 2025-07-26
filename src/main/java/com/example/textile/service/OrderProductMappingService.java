package com.example.textile.service;

import com.example.textile.dto.OrderProductMappingDto;
import com.example.textile.entity.Machine;
import com.example.textile.entity.OrderProductMapping;

import java.util.List;

public interface OrderProductMappingService {
    OrderProductMapping findById(Long id);
    List<OrderProductMapping> findByOrdersId(Long orderId);
    List<OrderProductMapping> saveOrderProductMapping(List<OrderProductMapping> orderProductMappings);

    OrderProductMappingDto saveOrderProductMappings(OrderProductMappingDto orderProductMappingDto);

    List<Machine> findMachineByMachineNo(String machineNo);

    Machine findMachineById(Long id);
}
