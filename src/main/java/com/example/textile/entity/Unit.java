package com.example.textile.entity;

import com.example.textile.enums.UnitOfMeasureEnum;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Unit implements Serializable {
    @Id
    private Long id;
    private String unitOfMeasure = UnitOfMeasureEnum.KG.getUnit();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
