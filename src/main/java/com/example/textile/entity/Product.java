package com.example.textile.entity;

import com.example.textile.utility.ShreeramTextileConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Product implements Serializable {
    private Long id;
    private String name;
    private Integer hsn = ShreeramTextileConstants.PRODUCT_HSN;
    private Date insertDt;
    private Date updateDt;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Column(unique = true)
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

    @CreationTimestamp
    public Date getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(Date insertDt) {
        this.insertDt = insertDt;
    }

    @UpdateTimestamp
    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
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
