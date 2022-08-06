package com.example.textile.entity;

import com.example.textile.utility.ShreeramTextile;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
public class Invoice implements Serializable {

    private Long id;
    private String invoiceNo;
    private Company invoiceBy = ShreeramTextile.getInfo().getCompanyDetails();
    private User user;
    private TransportMode transportMode;
    private Date invoiceDate = Calendar.getInstance().getTime();
    private String vehicleNo;
    private String reverseCharge = "No";
    private Date dateOfSupply;
    private String placeOfSupply;
    private Company billToParty;
    private Company shipToParty;
    private SaleType saleType;
    private List<ProductDetail> product;
    private BigDecimal pnfCharge = BigDecimal.ZERO;
    private BigDecimal totalAmount;
    private BigDecimal cGst;
    private BigDecimal sGst;
    private BigDecimal totalTaxAmount;
    private Double roundOff = (double) 0;
    private BigDecimal totalAmountAfterTax;
    private String totalInvoiceAmountInWords;
    private Date insertDate;
    private Date updateDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    @Transient
    public Company getInvoiceBy() {
        return invoiceBy;
    }

    public void setInvoiceBy(Company invoiceBy) {
        this.invoiceBy = invoiceBy;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    public TransportMode getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(TransportMode transportMode) {
        this.transportMode = transportMode;
    }

    @NotNull
    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getReverseCharge() {
        return reverseCharge;
    }

    public void setReverseCharge(String reverseCharge) {
        this.reverseCharge = reverseCharge;
    }

    public Date getDateOfSupply() {
        return dateOfSupply;
    }

    public void setDateOfSupply(Date dateOfSupply) {
        this.dateOfSupply = dateOfSupply;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    @ManyToOne
    public Company getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(Company billToParty) {
        this.billToParty = billToParty;
    }

    @ManyToOne
    public Company getShipToParty() {
        return shipToParty;
    }

    public void setShipToParty(Company shipToParty) {
        this.shipToParty = shipToParty;
    }

    @ManyToOne
    public SaleType getSaleType() {
        return saleType;
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = saleType;
    }

    @OneToMany(mappedBy = "id",orphanRemoval = true)
    public List<ProductDetail> getProduct() {
        return product;
    }

    public void setProduct(List<ProductDetail> product) {
        this.product = product;
    }

    public BigDecimal getPnfCharge() {
        return pnfCharge;
    }

    public void setPnfCharge(BigDecimal pnfCharge) {
        this.pnfCharge = pnfCharge;
    }

    @NotNull
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @NotNull
    public BigDecimal getcGst() {
        return cGst;
    }

    public void setcGst(BigDecimal cGst) {
        this.cGst = cGst;
    }

    @NotNull
    public BigDecimal getsGst() {
        return sGst;
    }

    public void setsGst(BigDecimal sGst) {
        this.sGst = sGst;
    }

    @NotNull
    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public Double getRoundOff() {
        return roundOff;
    }

    public void setRoundOff(Double roundOff) {
        this.roundOff = roundOff;
    }

    @NotNull
    public BigDecimal getTotalAmountAfterTax() {
        return totalAmountAfterTax;
    }

    public void setTotalAmountAfterTax(BigDecimal totalAmountAfterTax) {
        this.totalAmountAfterTax = totalAmountAfterTax;
    }

    @NotNull
    public String getTotalInvoiceAmountInWords() {
        return totalInvoiceAmountInWords;
    }

    public void setTotalInvoiceAmountInWords(String totalInvoiceAmountInWords) {
        this.totalInvoiceAmountInWords = totalInvoiceAmountInWords;
    }

    @CreationTimestamp
    @Column(updatable = false)
    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    @UpdateTimestamp
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", invoiceNo=" + invoiceNo +
                ", invoiceBy=" + invoiceBy +
                ", user=" + user +
                ", transportMode=" + transportMode +
                ", invoiceDate=" + invoiceDate +
                ", vehicleNo='" + vehicleNo + '\'' +
                ", reverseCharge=" + reverseCharge +
                ", dateOfSupply=" + dateOfSupply +
                ", placeOfSupply='" + placeOfSupply + '\'' +
                ", billToParty=" + billToParty +
                ", shipToParty=" + shipToParty +
                ", saleType=" + saleType +
                ", product=" + product +
                ", pnfCharge=" + pnfCharge +
                ", totalAmount=" + totalAmount +
                ", cGst=" + cGst +
                ", sGst=" + sGst +
                ", totalTaxAmount=" + totalTaxAmount +
                ", roundOff=" + roundOff +
                ", totalAmountAfterTax=" + totalAmountAfterTax +
                ", totalInvoiceAmountInWords='" + totalInvoiceAmountInWords + '\'' +
                '}';
    }
}
