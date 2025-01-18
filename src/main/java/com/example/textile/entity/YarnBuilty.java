package com.example.textile.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class YarnBuilty extends Document{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private CompanyYarnOrder companyYarnOrder;
    @Temporal(TemporalType.DATE)
    private LocalDate receivedDt;
    private Double loadUnloadCharges;
    private Integer boxes;
    @ManyToOne
    private Company tranportCompany;
    private String vehicleNo;
    private Double quantity;
}
