package com.example.textile.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Statement implements Serializable {
    private Long id;
    private Date insertDt;
    private Date txnDt;
    private BigDecimal amount;
    private String remarks;

    private BankDetail creditTo;
    private BankDetail debitFrom;

    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @CreationTimestamp
    public Date getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(Date insertDt) {
        this.insertDt = insertDt;
    }

    @Temporal(TemporalType.DATE)
    public Date getTxnDt() {
        return txnDt;
    }

    public void setTxnDt(Date txnDt) {
        this.txnDt = txnDt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    public BankDetail getCreditTo() {
        return creditTo;
    }

    public void setCreditTo(BankDetail creEditTo) {
        this.creditTo = creditTo;
    }

    @ManyToOne
    public BankDetail getDebitFrom() {
        return debitFrom;
    }

    public void setDebitFrom(BankDetail debitFrom) {
        this.debitFrom = debitFrom;
    }

    @Override
    public String toString() {
        return "Statement{" +
                "id=" + id +
                ", insertDt=" + insertDt +
                ", txnDt=" + txnDt +
                ", amount=" + amount +
                ", remarks='" + remarks + '\'' +
                ", creditTo=" + creditTo +
                ", debitFrom=" + debitFrom +
                ", user=" + user +
                '}';
    }
}
