package com.example.textile.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class BankStatement implements Serializable {
    private Long bankStatementId;
    private Date insertDt;
    private Date txnDt;
    private BigDecimal amount;
    private String description;

    private BankDetail creditTo;
    private BankDetail debitFrom;

    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getBankStatementId() {
        return bankStatementId;
    }

    public void setBankStatementId(Long bankStatementId) {
        this.bankStatementId = bankStatementId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setCreditTo(BankDetail creditTo) {
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
        return "BankStatement{" +
                "id=" + bankStatementId +
                ", insertDt=" + insertDt +
                ", txnDt=" + txnDt +
                ", amount=" + amount +
                ", remarks='" + description + '\'' +
                ", creditTo=" + creditTo.getId() +
                ", debitFrom=" + debitFrom.getId() +
                ", user=" + user.getId() +
                '}';
    }
}
