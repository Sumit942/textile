package com.example.textile.entity;

import java.math.BigDecimal;
import java.util.Date;

public class InvoiceView {
    private long id;
    private Date invoiceDate;
    private String invoiceNo;
    private String gst;
    private String name;
    private BigDecimal totalAmount;
    private BigDecimal cGst;
    private BigDecimal sGst;
    private Double roundOff;
    private BigDecimal pnfCharge;
    private BigDecimal totalAmountAfterTax;

    public InvoiceView(long id, Date invoiceDate, String invoiceNo, String gst, String name, BigDecimal totalAmount, BigDecimal cGst, BigDecimal sGst, Double roundOff, BigDecimal pnfCharge, BigDecimal totalAmountAfterTax) {
        this.id = id;
        this.invoiceDate = invoiceDate;
        this.invoiceNo = invoiceNo;
        this.gst = gst;
        this.name = name;
        this.totalAmount = totalAmount;
        this.cGst = cGst;
        this.sGst = sGst;
        this.roundOff = roundOff;
        this.pnfCharge = pnfCharge;
        this.totalAmountAfterTax = totalAmountAfterTax;
    }

    public long getId() {
        return id;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public String getGst() {
        return gst;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getcGst() {
        return cGst;
    }

    public BigDecimal getsGst() {
        return sGst;
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

    @Override
    public String toString() {
        return "InvoiceView{" +
                "id=" + id +
                ", invoiceDate=" + invoiceDate +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", gst='" + gst + '\'' +
                ", name='" + name + '\'' +
                ", totalAmount=" + totalAmount +
                ", cGst=" + cGst +
                ", sGst=" + sGst +
                ", roundOff=" + roundOff +
                ", pnfCharge=" + pnfCharge +
                ", totalAmountAfterTax=" + totalAmountAfterTax +
                '}';
    }
}
