package com.example.textile.controller;

import com.example.textile.dto.OrdersDto;
import com.example.textile.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("order")
public class OrderController {

    private final OrdersService ordersService;

    public OrderController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @GetMapping //TODO: restrict to only admin
    public ResponseEntity<List<OrdersDto>> fetchAll() {
        log.info("Entry fetching all orders");
        List<OrdersDto> ordersDtos = ordersService.findAll();
        return new ResponseEntity<>(ordersDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrdersDto> saveOrder(@RequestBody OrdersDto ordersDto) {
        log.info("Entry saving Order");

        OrdersDto savedOrder = ordersService.saveOrUpdate(ordersDto);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }
}
