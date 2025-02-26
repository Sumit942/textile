package com.example.textile.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
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
    @ManyToMany(mappedBy = "yarnOrderItemProducts")
    private List<YarnOrderItem> yarnOrderItems;
}
