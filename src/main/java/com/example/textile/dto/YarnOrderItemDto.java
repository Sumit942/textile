package com.example.textile.dto;

import com.example.textile.entity.Yarn;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class YarnOrderItemDto {
    private Long id;
    private Yarn yarn;
    private Long companyId;
    private String companyName;
    private BigDecimal quantity;
    private BigDecimal qtyAllocated;
    private BigDecimal qtyLeft;
    private Integer boxes;
}
