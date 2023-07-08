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
    private String vehicleNo = "NA";
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
    private BigDecimal iGst;
    private Double gstPerc = 5.0;

    private BigDecimal totalTaxAmount;
    private Double roundOff = (double) 0;
    private BigDecimal totalAmountAfterTax;
    private String totalInvoiceAmountInWords;
    private Date insertDate;
    private Date updateDate;
    private Integer version;

    //bank select from dropdown
    private BankDetail selectedBank;
    private List<Challan> challans;

    @Transient
    public Company getInvoiceBy() {
        return invoiceBy;
    }

    public void setInvoiceBy(Company invoiceBy) {
        this.invoiceBy = invoiceBy;
    }

    @ManyToOne
    public BankDetail getSelectedBank() {
        return selectedBank;
    }

    public void setSelectedBank(BankDetail selectedBank) {
        this.selectedBank = selectedBank;
    }

    @OneToMany(mappedBy = "invoice")
    public List<Challan> getChallans() {
        return challans;
    }

    public void setChallans(List<Challan> challans) {
        this.challans = challans;
    }

    @Transient
    public boolean isNew() {
        return id == null || id <= 0;
    }

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
    @Temporal(TemporalType.DATE)
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

    @Temporal(TemporalType.DATE)
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

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_id",referencedColumnName = "id")
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

    public BigDecimal getiGst() {
        return iGst;
    }

    public void setiGst(BigDecimal iGst) {
        this.iGst = iGst;
    }

    @Column(columnDefinition = "double default 5")
    public Double getGstPerc() {
        return gstPerc;
    }

    public void setGstPerc(Double gstPerc) {
        this.gstPerc = gstPerc;
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

    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", invoiceNo=" + invoiceNo +
//                ", invoiceBy=" + invoiceBy +
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
                ", iGst=" + iGst +
                ", gstPerc=" + gstPerc +
                ", totalTaxAmount=" + totalTaxAmount +
                ", roundOff=" + roundOff +
                ", totalAmountAfterTax=" + totalAmountAfterTax +
                ", totalInvoiceAmountInWords='" + totalInvoiceAmountInWords +
                ", selectedBank='" + selectedBank +
                '}';
    }
}
