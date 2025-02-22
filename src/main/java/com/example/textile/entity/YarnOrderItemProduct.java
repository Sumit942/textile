package com.example.textile.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class YarnOrderItemProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JsonBackReference
    private YarnOrderItem yarnOrderItem;
    @ManyToOne
    private YarnFabricDesign yarnFabricDesign;
    private Double quantity;
    @ManyToOne
    private Machine machine;
    private String remarks;
}
