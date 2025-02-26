package com.example.textile.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@ToString(exclude = "yarnFabricDesign")
public class FabricDesignYarnMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Yarn yarn;
    @ManyToOne
    private YarnFabricDesign yarnFabricDesign;
}
