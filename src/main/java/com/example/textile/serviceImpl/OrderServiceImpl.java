package com.example.textile.serviceImpl;

import com.example.textile.dto.OrdersDto;
import com.example.textile.entity.Orders;
import com.example.textile.repo.OrdersRepository;
import com.example.textile.service.OrdersService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrdersService {
    private OrdersRepository ordersRepo;
    private ModelMapper modelMapper;

    @Override
    public List<OrdersDto> findAll() {
        List<Orders> orders = ordersRepo.findAll();
        return modelMapper.map(orders, new TypeToken<List<OrdersDto>>(){}.getType());
    }

    @Override
    public OrdersDto saveOrUpdate(OrdersDto ordersDto) {
        Orders order = modelMapper.map(ordersDto, Orders.class);
        Orders savedOrder = ordersRepo.save(order);
        return modelMapper.map(savedOrder, OrdersDto.class);
    }

    @Override
    public OrdersDto findById(Long id) {
        Orders order = ordersRepo.findById(id).orElse(null);
        return modelMapper.map(order, OrdersDto.class);
    }
}
