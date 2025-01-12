package com.example.textile.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
public class Yarn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String type;
    @ManyToOne
    private Company company;
    @Column(name = "rate", precision = 10, scale = 2)
    private BigDecimal rate;
    private String description;
    @CreationTimestamp
    private Date insertDt;
    @UpdateTimestamp
    private Date updateDt;
}
