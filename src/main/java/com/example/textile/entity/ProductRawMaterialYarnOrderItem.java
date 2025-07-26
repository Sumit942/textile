package com.example.textile.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ProductRawMaterialYarnOrderItem {
    @EmbeddedId
    private ProductRawMaterialYarnOrderItemId id;
    @ManyToOne
    @MapsId("productRawMaterialId")
    private ProductRawMaterial productRawMaterial;
    @ManyToOne
    @MapsId("yarnOrderItemId")
    private YarnOrderItem yarnOrderItem;
    private BigDecimal qty = BigDecimal.ZERO;

    public ProductRawMaterialYarnOrderItem(ProductRawMaterial material, YarnOrderItem item, BigDecimal qty) {
        this.productRawMaterial = material;
        this.yarnOrderItem  = item;
        this.qty = qty;
        this.id = new ProductRawMaterialYarnOrderItemId(material.getId(), item.getId());
    }
}
