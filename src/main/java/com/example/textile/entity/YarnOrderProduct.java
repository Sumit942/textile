package com.example.textile.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class YarnOrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Product product;
    private String gsm;
    private Double quantity;
    @ManyToOne
    private YarnOrderItem yarnOrderItem;
}
