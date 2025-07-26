package com.example.textile.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class ProductRawMaterialYarnOrderItemId implements Serializable {
    private Long productRawMaterialId;
    private Long yarnOrderItemId;
    public ProductRawMaterialYarnOrderItemId(Long productRawMaterialId, Long yarnOrderItemId) {
        this.productRawMaterialId = productRawMaterialId;
        this.yarnOrderItemId = yarnOrderItemId;
    }
}