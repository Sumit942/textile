package com.example.textile.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Setter
@Entity
public class Company implements Serializable {
    private Long id;
    private String name;
    @OneToOne(fetch = FetchType.LAZY)
    private Address address;
    @OneToOne(fetch = FetchType.LAZY)
    private Address ofcAddress;
    private String gst;
    @Getter
    private String emailId;
    @Getter
    private String mobileNo;
    private List<BankDetail> bankDetails;
    private Date insert_dt;
    private Date update_dt;
    @ManyToOne(fetch = FetchType.LAZY)
    private CompanyType companyType;
    @Getter
    private String code;

    @OneToOne
    public CompanyType getCompanyType() {
        return companyType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public Address getAddress() {
        return address;
    }

    @OneToOne
    public Address getOfcAddress() {
        return ofcAddress;
    }

    @Column(length = 15, unique = true, nullable = false)
    public String getGst() {
        return gst;
    }

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "company_id")
    @JsonManagedReference
    public List<BankDetail> getBankDetails() {
        return bankDetails;
    }

    @CreationTimestamp
    public Date getInsert_dt() {
        return insert_dt;
    }

    @UpdateTimestamp
    public Date getUpdate_dt() {
        return update_dt;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gst='" + gst + '\'' +
                ", emailId='" + emailId + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
//                ", bankDetails=" + bankDetails +
                '}';
    }
}
