package com.example.textile.dto;

import com.example.textile.enums.OrderStatusType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto {
    private Long id;
    @JsonManagedReference
    private List<CompanyYarnOrderDto> companyYarnOrders;
    private CompanyDto company;
    private OrderStatusType orderStatusType = OrderStatusType.RECEIVED;
    private String remarks;
    private Long version;
}
