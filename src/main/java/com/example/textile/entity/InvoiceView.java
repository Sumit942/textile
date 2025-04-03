package com.example.textile.entity;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Entity
@Immutable
public class InvoiceView {

    private Long id;
    private long invoiceId;
    private Date invoiceDate;
    private String invoiceNo;
    private Long billToPartyId;
    private String billToPartyGst;
    private String billToPartyName;
    private BigDecimal totalAmount;
    private BigDecimal totalTaxAmount;
    private Double roundOff;
    private BigDecimal pnfCharge;
    private BigDecimal totalAmountAfterTax;
    private Boolean paid;
    private BigDecimal paidAmount;
    private BigDecimal amtDr;
    private Date paymentDt;

    private Long pendingDays;

    @Transient
    public Long getPendingDays() {
        return pendingDays;
    }

    @Id
    public Long getId() {
        return id;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    @Temporal(TemporalType.DATE)
    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public String getBillToPartyGst() {
        return billToPartyGst;
    }

    public String getBillToPartyName() {
        return billToPartyName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public Double getRoundOff() {
        return roundOff;
    }

    public BigDecimal getPnfCharge() {
        return pnfCharge;
    }

    public BigDecimal getTotalAmountAfterTax() {
        return totalAmountAfterTax;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setInvoiceId(long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
        if (invoiceDate != null) {
            LocalDate invoiceLocalDate = new Date(this.invoiceDate.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            this.pendingDays = ChronoUnit.DAYS.between(invoiceLocalDate, LocalDate.now(ZoneId.systemDefault()));
        }
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Long getBillToPartyId() {
        return billToPartyId;
    }

    public void setBillToPartyId(Long billToPartyId) {
        this.billToPartyId = billToPartyId;
    }

    public void setBillToPartyGst(String billToPartyGst) {
        this.billToPartyGst = billToPartyGst;
    }

    public void setBillToPartyName(String billToPartyName) {
        this.billToPartyName = billToPartyName;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public void setRoundOff(Double roundOff) {
        this.roundOff = roundOff;
    }

    public void setPnfCharge(BigDecimal pnfCharge) {
        this.pnfCharge = pnfCharge;
    }

    public void setTotalAmountAfterTax(BigDecimal totalAmountAfterTax) {
        this.totalAmountAfterTax = totalAmountAfterTax;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getAmtDr() {
        return amtDr;
    }

    public void setAmtDr(BigDecimal amtDr) {
        this.amtDr = amtDr;
    }

    public Date getPaymentDt() {
        return paymentDt;
    }

    public void setPaymentDt(Date paymentDt) {
        this.paymentDt = paymentDt;
    }

    @Override
    public String toString() {
        return "InvoiceView{" +
                "id=" + id +
                ", invoiceId=" + invoiceId +
                ", invoiceDate=" + invoiceDate +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", billToPartyId=" + billToPartyId +
                ", billToPartyGst='" + billToPartyGst + '\'' +
                ", billToPartyName='" + billToPartyName + '\'' +
                ", totalAmount=" + totalAmount +
                ", totalTaxAmount=" + totalTaxAmount +
                ", roundOff=" + roundOff +
                ", pnfCharge=" + pnfCharge +
                ", totalAmountAfterTax=" + totalAmountAfterTax +
                ", paid=" + paid +
                ", paidAmount=" + paidAmount +
                ", amtDr=" + amtDr +
                ", paymentDt=" + paymentDt +
                '}';
    }
}
