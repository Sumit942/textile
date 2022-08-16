package com.example.textile.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Company implements Serializable {
    private Long id;
    private String name;
    private Address address;
    private Address ofcAddress;
    private String gst;
    private String emailId;
    private String mobileNo;
    private List<BankDetail> bankDetails;
    private Date insert_dt;
    private Date update_dt;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @OneToOne
    public Address getOfcAddress() {
        return ofcAddress;
    }

    public void setOfcAddress(Address ofcAddress) {
        this.ofcAddress = ofcAddress;
    }

    @Column(length = 15, unique = true, nullable = false)
    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "company_id")
    public List<BankDetail> getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(List<BankDetail> bankDetails) {
        this.bankDetails = bankDetails;
    }

    @CreationTimestamp
    public Date getInsert_dt() {
        return insert_dt;
    }

    public void setInsert_dt(Date insert_dt) {
        this.insert_dt = insert_dt;
    }

    @UpdateTimestamp
    public Date getUpdate_dt() {
        return update_dt;
    }

    public void setUpdate_dt(Date update_dt) {
        this.update_dt = update_dt;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address=" + address +
                ", gst='" + gst + '\'' +
                ", emailId='" + emailId + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", bankDetails=" + bankDetails +
                '}';
    }
}
