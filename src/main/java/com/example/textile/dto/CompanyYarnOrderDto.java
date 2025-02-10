package com.example.textile.dto;

import com.example.textile.entity.Orders;
import com.example.textile.entity.YarnBuilty;
import com.example.textile.entity.YarnOrderItem;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class CompanyYarnOrderDto {
    public CompanyYarnOrderDto(Long id) {
        this.id = id;
    }
    private Long id;
    private List<YarnOrderItem> yarnOrderItems;
    private List<YarnBuilty> yarnBuilties;
    private Orders order;
    private Date orderDt;
    private String yarnInvoiceNo;
    private String remark;
    private Double totalQuantity;
    private BigDecimal totalAmount;
    private Long version;
}
