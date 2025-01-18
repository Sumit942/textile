package com.example.textile.dto;

import com.example.textile.entity.Orders;
import com.example.textile.entity.YarnOrderItem;
import lombok.Data;

import java.util.List;

@Data
public class CompanyYarnOrderDto {
    private Long id;
    private List<YarnOrderItem> yarnOrderItems;
    private Orders orders;
    private Double loadUnloadCharges;
    private String vehicleNo;
    private CompanyDto transport;
    private List<YarnOrderItem> productDesigns;
}
