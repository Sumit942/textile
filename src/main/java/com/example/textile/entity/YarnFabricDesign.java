package com.example.textile.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class YarnFabricDesign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany
    private List<FabricDesignYarnMapping> yarns;
    @ManyToOne
    private FabricDesign fabricDesign;
    private String gsm;
}
