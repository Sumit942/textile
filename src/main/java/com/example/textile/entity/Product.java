package com.example.textile.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Product implements Serializable {
    @Id
    private Long id;
    private String name;
    private Integer hsn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHsn() {
        return hsn;
    }

    public void setHsn(Integer hsn) {
        this.hsn = hsn;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hsn=" + hsn +
                '}';
    }
}
