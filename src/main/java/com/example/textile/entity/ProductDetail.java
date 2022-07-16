package com.example.textile.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class ProductDetail implements Serializable {
    @Id
    private Long id;
    @Column(nullable = false)
    @ManyToOne
    private Product product;
    private String chNo;
    @Column(nullable = false)
    @ManyToOne
    private Unit unitOfMeasure;
    @Column(nullable = false)
    private Double quantity;
    @Column(nullable = false)
    private Double rate;
    @Column(nullable = false)
    private BigDecimal totalPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Unit getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(Unit unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

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
