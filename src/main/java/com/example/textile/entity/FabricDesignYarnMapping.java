package com.example.textile.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class FabricDesignYarnMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Yarn yarn;
    @ManyToOne
    private YarnFabricDesign yarnFabricDesign;
}
