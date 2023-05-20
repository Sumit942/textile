package com.example.textile.entity;

import com.example.textile.enums.PaymentModeEnum;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class BankStatement implements Serializable {
    private Long bankStatementId;
    private Date insertDt;
    private Date txnDt;
    private BigDecimal amount;
    private String description;
    private String paymentMode;
    private String name;

    private BankDetail creditTo;
    private BankDetail debitFrom;

    private User user;

    public BankStatement() {
        this.paymentMode = PaymentModeEnum.NEFT.name();
    }
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
    @NotNull
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

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", paymentMode=" + paymentMode +
                ", name=" + name +
                ", user=" + user.getId() +
                '}';
    }
}
