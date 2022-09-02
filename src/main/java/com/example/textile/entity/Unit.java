package com.example.textile.entity;

import com.example.textile.enums.UnitOfMeasureEnum;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
public class Unit implements Serializable {
    private Long id;
    private String unitOfMeasure = UnitOfMeasureEnum.KG.getUnit();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "id=" + id +
                ", unitOfMeasure='" + unitOfMeasure + '\'' +
                '}';
    }
}
