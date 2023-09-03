package com.example.textile.entity;

import javax.persistence.*;

@Entity
public class YarnDesign {

    private Long id;
    private String design;


    public String getDesign() {
        return design;
    }

    public void setDesign(String design) {
        this.design = design;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
