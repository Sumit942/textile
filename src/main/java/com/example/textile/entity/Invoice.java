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
    private Long invoiceNo;
    @ManyToOne
    private Company invoiceBy;
    @ManyToOne
    private User user;
    @ManyToOne
    private TransportMode transportMode;
    private Date invoiceDate;
    private String vehicleNo;
    private final Boolean reverseCharge = Boolean.FALSE;
    private Date dateOfSupply;
    @ManyToOne
    private State state;
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

    public Long getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(Long invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Company getInvoiceBy() {
        return invoiceBy;
    }

    public void setInvoiceBy(Company invoiceBy) {
        this.invoiceBy = invoiceBy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TransportMode getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(TransportMode transportMode) {
        this.transportMode = transportMode;
    }

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

    public Boolean getReverseCharge() {
        return reverseCharge;
    }

    public Date getDateOfSupply() {
        return dateOfSupply;
    }

    public void setDateOfSupply(Date dateOfSupply) {
        this.dateOfSupply = dateOfSupply;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public Company getBillToParty() {
        return billToParty;
    }

    public void setBillToParty(Company billToParty) {
        this.billToParty = billToParty;
    }

    public Company getShipToParty() {
        return shipToParty;
    }

    public void setShipToParty(Company shipToParty) {
        this.shipToParty = shipToParty;
    }

    public SaleType getSaleType() {
        return saleType;
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = saleType;
    }

    public List<ProductDetail> getProduct() {
        return product;
    }

    public void setProduct(List<ProductDetail> product) {
        this.product = product;
    }

    public Double getPnfCharge() {
        return pnfCharge;
    }

    public void setPnfCharge(Double pnfCharge) {
        this.pnfCharge = pnfCharge;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getcGst() {
        return cGst;
    }

    public void setcGst(BigDecimal cGst) {
        this.cGst = cGst;
    }

    public BigDecimal getsGst() {
        return sGst;
    }

    public void setsGst(BigDecimal sGst) {
        this.sGst = sGst;
    }

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

    public BigDecimal getTotalAmountAfterTax() {
        return totalAmountAfterTax;
    }

    public void setTotalAmountAfterTax(BigDecimal totalAmountAfterTax) {
        this.totalAmountAfterTax = totalAmountAfterTax;
    }

    public String getTotalInvoiceAmountInWords() {
        return totalInvoiceAmountInWords;
    }

    public void setTotalInvoiceAmountInWords(String totalInvoiceAmountInWords) {
        this.totalInvoiceAmountInWords = totalInvoiceAmountInWords;
    }

    public BankDetail getBankDetail() {
        return bankDetail;
    }

    public void setBankDetail(BankDetail bankDetail) {
        this.bankDetail = bankDetail;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceNo=" + invoiceNo +
                ", invoiceBy=" + invoiceBy +
                ", user=" + user +
                ", transportMode=" + transportMode +
                ", invoiceDate=" + invoiceDate +
                ", vehicleNo='" + vehicleNo + '\'' +
                ", reverseCharge=" + reverseCharge +
                ", dateOfSupply=" + dateOfSupply +
                ", state=" + state +
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
                ", bankDetail=" + bankDetail +
                '}';
    }
}
