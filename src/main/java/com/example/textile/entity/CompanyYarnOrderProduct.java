package com.example.textile.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
public class CompanyYarnOrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private YarnFabricDesign yarnFabricDesign;
    @ManyToOne
    private Machine machine;
    private String remarks;
    @OneToMany(mappedBy = "companyYarnOrderProduct")
    private List<CompanyYarnOrderProductMapping> companyYarnOrderProductMappings;
}
