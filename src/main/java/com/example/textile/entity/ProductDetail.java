package com.example.textile.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
public class ProductDetail implements Serializable {
    private Long id;
    private Product product;
    private Long chNo;
    private Unit unitOfMeasure;
    private Double quantity;
    private Double rate;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private Invoice invoice;
    private Company party;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getChNo() {
        return chNo;
    }

    public void setChNo(Long chNo) {
        this.chNo = chNo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
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

    @Column(nullable = false)
    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Column(nullable = false)
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    @JsonBackReference
    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Company getParty() {
        return party;
    }

    public void setParty(Company party) {
        this.party = party;
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
        return "ProductDetail{" + "id=" + id + ", product=" + product + ", chNo='" + chNo + '\'' + ", unitOfMeasure=" + unitOfMeasure + ", party=" + party +", quantity=" + quantity + ", rate=" + rate + ", totalPrice=" + totalPrice + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDetail that = (ProductDetail) o;
        return Objects.equals(id, that.id) && product.equals(that.product) && chNo.equals(that.chNo) && quantity.equals(that.quantity) && rate.equals(that.rate) && totalPrice.compareTo(that.totalPrice)==0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, chNo, quantity, rate, totalPrice);
    }
}
