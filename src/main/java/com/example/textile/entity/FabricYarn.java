package com.example.textile.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class FabricYarn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Yarn yarn;
}
