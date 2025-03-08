package com.example.textile.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
public class FabricRoll extends Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer rollNo;
    @ManyToOne
    private CompanyYarnOrderProductMapping companyYarnOrderProductMapping;
    private Boolean isWhite = Boolean.FALSE;
    private Double quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challan_id")
    private Challan challan;
    @ManyToOne
    private Employee employee;
}
