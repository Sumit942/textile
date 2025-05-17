package com.example.textile.dto;

import com.example.textile.entity.CompanyYarnOrderProduct;
import com.example.textile.entity.ProductRawMaterial;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class OrderProductMappingDto {
    private Long id;
    private OrdersDto orders;
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Setter
    @Getter
    public static class OrderProduct {
        private Long id;
        private CompanyYarnOrderProduct companyYarnOrderProduct;
        private List<ProductRawMaterial> rawMaterials;
        private BigDecimal quantity;
    }
}
