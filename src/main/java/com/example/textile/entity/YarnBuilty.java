package com.example.textile.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
public class YarnBuilty extends Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonBackReference
    private CompanyYarnOrder companyYarnOrder;
    @Temporal(TemporalType.DATE)
    private Date receivedDt;
    private Double loadUnloadCharges;
    private Integer boxes;
    @ManyToOne(fetch = FetchType.LAZY)
    private Company tranportCompany;
    private String vehicleNo;
    private Double quantity;
}
