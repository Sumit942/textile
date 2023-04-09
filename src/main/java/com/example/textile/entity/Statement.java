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

    private Employee employee;

    private BankDetail debitFrom;
    private BankDetail creditTo;
    private StatementType type;

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

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date getTxnDt() {
        return txnDt;
    }

    public void setTxnDt(Date txnDt) {
        this.txnDt = txnDt;
    }

    @Column
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @ManyToOne
    @JoinColumn(name = "employee_id",nullable = false)
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @ManyToOne
    public BankDetail getDebitFrom() {
        return debitFrom;
    }

    public void setDebitFrom(BankDetail debitFrom) {
        this.debitFrom = debitFrom;
    }

    @ManyToOne
    public BankDetail getCreditTo() {
        return creditTo;
    }

    public void setCreditTo(BankDetail creditTo) {
        this.creditTo = creditTo;
    }

    @ManyToOne
    public StatementType getType() {
        return type;
    }

    public void setType(StatementType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Statement{" +
                "id=" + id +
                ", insertDt=" + insertDt +
                ", txnDt=" + txnDt +
                ", amount=" + amount +
                ", remarks='" + remarks + '\'' +
                ", employee ='" + employee.getId() + '\'' +
                ", debitFrom='" + debitFrom.getId() + '\'' +
                ", creditTo='" + creditTo.getId() + '\'' +
                ", type='" + type.getType() + '\'' +
                '}';
    }
}
