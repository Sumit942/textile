package com.example.textile.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class OrderProductMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private CompanyYarnOrderProduct companyYarnOrderProduct;
    @OneToMany(mappedBy = "orderProductMapping")
    private List<ProductRawMaterial> rawMaterial;
    private Double quantity;
}
