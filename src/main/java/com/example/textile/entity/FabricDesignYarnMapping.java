package com.example.textile.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Setter
@Getter
@Entity
public class FabricDesignYarnMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Yarn yarn;
    @ManyToOne
    @JsonBackReference
    private YarnFabricDesign yarnFabricDesign;
    @DecimalMin("0.01")
    @DecimalMax("100.00")
    @Column(precision = 5, scale = 2)
    private BigDecimal percentage;
}
