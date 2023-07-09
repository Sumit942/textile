package com.example.textile.entity;

import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Immutable
public class InvoiceView {

    private Long id;
    private long invoiceId;
    private Date invoiceDate;
    private String invoiceNo;
    private String billToPartyId;
    private String billToPartyGst;
    private String billToPartyName;
    private BigDecimal totalAmount;
    private BigDecimal totalTaxAmount;
    private Double roundOff;
    private BigDecimal pnfCharge;
    private BigDecimal totalAmountAfterTax;

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
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getBillToPartyId() {
        return billToPartyId;
    }

    public void setBillToPartyId(String billToPartyId) {
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

    @Override
    public String toString() {
        return "InvoiceView{" +
                "id=" + id +
                ", invoiceId=" + invoiceId +
                ", invoiceDate=" + invoiceDate +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", billToPartyGst='" + billToPartyGst + '\'' +
                ", billToPartyName='" + billToPartyName + '\'' +
                ", totalAmount=" + totalAmount +
                ", totalTaxAmount=" + totalTaxAmount +
                ", roundOff=" + roundOff +
                ", pnfCharge=" + pnfCharge +
                ", totalAmountAfterTax=" + totalAmountAfterTax +
                '}';
    }
}
