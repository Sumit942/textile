package com.example.textile.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class Invoice implements Serializable {

    @Id
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private TransportMode transportMode;
    private Date invoiceDate;
    private String vehicleNo;
    private Boolean reverseCharge = Boolean.FALSE;
    private Date dateOfSupply;
    private String state;
    private Integer code;
    private String placeOfSupply;
    @ManyToOne
    private Company billToParty;
    @ManyToOne
    private Company shipToParty;
    @ManyToOne
    private SaleType saleType;
    @OneToMany
    private List<ProductDetail> product;
    private Double pnfCharge;
    private BigDecimal totalAmount;
    private BigDecimal cGst;
    private BigDecimal sGst;
    private BigDecimal totalTaxAmount;
    private Double roundOff;
    private BigDecimal totalAmountAfterTax;
    private String totalInvoiceAmountInWords;
    @ManyToOne
    private BankDetail bankDetail;
}
