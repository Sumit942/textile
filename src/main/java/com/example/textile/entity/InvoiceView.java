package com.example.textile.entity;

import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.util.Date;

@Projection(name = "invoiceView",types = {Invoice.class})
public interface InvoiceView {

    public Long getId();

    public Date getInvoiceDate();

    public String getInvoiceNo();

    public String getGst();

    public String getName();

    public BigDecimal getTotalAmount();

    public BigDecimal getcGst();

    public BigDecimal getsGst();

    public Double getRoundOff();

    public BigDecimal getPnfCharge();

    public BigDecimal getTotalAmountAfterTax();

}
