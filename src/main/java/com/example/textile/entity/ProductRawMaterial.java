package com.example.textile.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class ProductRawMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "productRawMaterial", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductRawMaterialYarnOrderItem> yarnOrderItems = new ArrayList<>();
    @DecimalMin("0.01")
    @DecimalMax("100.00")
    @Column(precision = 5, scale = 2)
    private BigDecimal percentage;
    @ManyToOne
    private OrderProductMapping orderProductMapping;

}
