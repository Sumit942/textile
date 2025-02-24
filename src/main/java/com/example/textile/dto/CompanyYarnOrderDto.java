package com.example.textile.dto;

import com.example.textile.entity.YarnBuilty;
import com.example.textile.entity.YarnOrderItem;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = {"yarnOrderItems","yarnBuilties","order"})
public class CompanyYarnOrderDto {
    public CompanyYarnOrderDto(Long id) {
        this.id = id;
    }
    private Long id;
    private List<YarnOrderItem> yarnOrderItems;
    private List<YarnBuilty> yarnBuilties;
    @JsonBackReference
    private OrdersDto order;
    private Date orderDt;
    private String yarnInvoiceNo;
    private String remark;
    private Double totalQuantity;
    private BigDecimal totalAmount;
    private Long version;
    private CompanyDto company;
}
