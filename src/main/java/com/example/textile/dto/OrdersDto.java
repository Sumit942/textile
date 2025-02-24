package com.example.textile.dto;

import com.example.textile.enums.OrderStatusType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class OrdersDto {
    public OrdersDto(Long id, String orderNo) {
        this.id = id;
        this.orderNo = orderNo;
    }
    private Long id;
    @JsonManagedReference
    private List<CompanyYarnOrderDto> companyYarnOrders;
    private CompanyDto company;
    private OrderStatusType orderStatusType = OrderStatusType.RECEIVED;
    private String remarks;
    private Long version;
    private String orderNo;
}
