package com.example.textile.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
public class YarnBuilty extends Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
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
