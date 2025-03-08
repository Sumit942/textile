package com.example.textile.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
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
    private Date insertDt;
    private Date updateDt;
    private CompanyType companyType;
    private String code;

    @PrePersist
    private void prePersist() {
        System.out.println("Company.prePersist");
        if (StringUtils.isEmpty(code)) {
            StringBuilder sbCode = new StringBuilder();
            for (String s : name.split("\\s+")) {
                char c = s.charAt(0);
                if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                    continue;
                }
                sbCode.append(c);
            }
            setCode(sbCode.toString().toUpperCase());
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
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
    public Date getInsertDt() {
        return insertDt;
    }

    @UpdateTimestamp
    public Date getUpdateDt() {
        return updateDt;
    }

    @Column(unique = true, nullable = false)
    public String getCode() {
        return code;
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
