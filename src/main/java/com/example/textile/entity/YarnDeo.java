package com.example.textile.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class YarnDeo {

    private Long id;
    private BigDecimal quantity;
    private BigDecimal rate;
    private BigDecimal amount;
    private Integer noOfBags;
    private Yarn yarn;
    private BigDecimal totalWeight;
    private Company partyName;
    private String remarks;
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Integer getNoOfBags() {
        return noOfBags;
    }

    @ManyToOne
    public Yarn getYarn() {
        return yarn;
    }

    @NotNull
    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    @NotNull
    @ManyToOne
    public Company getPartyName() {
        return partyName;
    }

    public String getRemarks() {
        return remarks;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setNoOfBags(Integer noOfBags) {
        this.noOfBags = noOfBags;
    }

    public void setYarn(Yarn yarn) {
        this.yarn = yarn;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public void setPartyName(Company partyName) {
        this.partyName = partyName;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
