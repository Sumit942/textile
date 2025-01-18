package com.example.textile.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
public class YarnOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Yarn yarn;
    @ManyToOne
    private CompanyYarnOrder companyYarnOrder;
    @Column(nullable = false)
    private Double quantity;
    private Integer boxes;
    private Double rate;
    private String hsn;
    private String lotNo;
    private BigDecimal amount;
    @OneToMany(mappedBy = "yarnOrderItem")
    private List<YarnOrderItemProduct> yarnOrderItemProducts;
}
