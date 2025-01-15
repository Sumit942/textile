package com.example.textile.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class YarnOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Yarn yarn;
    @ManyToOne
    private YarnOrder yarnOrder;
    @Column(nullable = false)
    private Double quantity;
    private Integer boxes;
    private Double rate;
    @OneToMany(mappedBy = "yarnOrderItem")
    private List<YarnOrderProduct> yarnOrderProducts;
}
