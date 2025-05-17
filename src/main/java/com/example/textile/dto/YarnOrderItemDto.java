package com.example.textile.dto;

import com.example.textile.entity.Yarn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YarnOrderItemDto {
    private Long id;
    private Yarn yarn;
    private Long companyId;
    private String companyName;
    private Double quantity;
    private Double qtyAllocated;
    private Double qtyUsed;
    private Integer boxes;
}
