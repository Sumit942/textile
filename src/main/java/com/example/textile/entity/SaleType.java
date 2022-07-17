package com.example.textile.entity;

import com.example.textile.enums.SaleTypeEnum;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class SaleType implements Serializable {
    private Long id;
    private String saleType = SaleTypeEnum.FJW.getSaleType();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    @Override
    public String toString() {
        return "SaleType{" +
                "id=" + id +
                ", saleType='" + saleType + '\'' +
                '}';
    }
}
