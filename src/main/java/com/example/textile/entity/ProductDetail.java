package com.example.textile.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class ProductDetail implements Serializable {
    private Long id;
    private Product product;
    private String chNo;
    private Unit unitOfMeasure;
    private Double quantity;
    private Double rate;
    private BigDecimal totalPrice;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getChNo() {
        return chNo;
    }

    public void setChNo(String chNo) {
        this.chNo = chNo;
    }

    @ManyToOne
    public Unit getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(Unit unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    @NotNull
    @Column(nullable = false)
    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @NotNull
    @Column(nullable = false)
    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @NotNull
    @Column(nullable = false)
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "ProductDetail{" +
                "id=" + id +
                ", product=" + product +
                ", chNo='" + chNo + '\'' +
                ", unitOfMeasure=" + unitOfMeasure +
                ", quantity=" + quantity +
                ", rate=" + rate +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
