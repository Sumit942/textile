package com.example.textile.dto;

import com.example.textile.enums.OrderStatusType;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto {
    private Long id;
    private List<CompanyYarnOrderDto> companyYarnOrders;
    private CompanyDto company;
    private OrderStatusType orderStatusType = OrderStatusType.RECEIVED;
    private String remarks;
    private Long version;
}
