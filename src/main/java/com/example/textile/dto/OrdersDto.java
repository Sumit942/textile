package com.example.textile.dto;

import com.example.textile.entity.User;
import com.example.textile.enums.OrderStatusType;
import lombok.Data;

import javax.persistence.ManyToOne;
import java.util.List;

@Data
public class OrdersDto {
    private Long id;
    private List<YarnOrderDto> yarnOrders;
    private CompanyDto company;
    private OrderStatusType orderStatusType = OrderStatusType.RECEIVED;
    private String remarks;
    @ManyToOne  //TODO: nullable false
    private User user;
}
