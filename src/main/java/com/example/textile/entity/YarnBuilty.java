package com.example.textile.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
public class YarnBuilty extends Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private CompanyYarnOrder companyYarnOrder;
    @Temporal(TemporalType.DATE)
    private Date receivedDt;
    private Double loadUnloadCharges;
    private Integer boxes;
    @ManyToOne
    private Company tranportCompany;
    private String vehicleNo;
    private Double quantity;
}
