package com.example.textile.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class CompanyYarnOrderProductMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private CompanyYarnOrder companyYarnOrder;
    @ManyToOne
    private CompanyYarnOrderProduct companyYarnOrderProduct;
    private Double quantity;
}
