package com.example.textile.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@Entity
public class Yarn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String type;
    private String companyName;
    @Column(name = "rate", precision = 10, scale = 2)
    private BigDecimal rate;
    private String description;
}
