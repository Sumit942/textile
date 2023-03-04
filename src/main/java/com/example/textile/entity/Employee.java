package com.example.textile.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
public class Employee implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private Designation designation;
    private BigDecimal salary;
    private String accountNo;
    private String ifscCode;
    private String panCardNo;

    private List<Statement> statements;

    private Address address;
    private Date insertDt;
    private Date updateDt;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @ManyToOne
    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    @Column
    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    @Column
    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    @Column
    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    //TODO: uncomment the below code after employee flow complete
//    @Column(unique = true, nullable = false)
    @Column
    public String getPanCardNo() {
        return panCardNo;
    }

    public void setPanCardNo(String panCardNo) {
        this.panCardNo = panCardNo;
    }

    @OneToMany(mappedBy = "employee")
    public List<Statement> getStatements() {
        return statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }


    @OneToOne(cascade = CascadeType.ALL)
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @CreationTimestamp
    public Date getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(Date insertDt) {
        this.insertDt = insertDt;
    }

    @UpdateTimestamp
    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", designation=" + designation +
                ", salary=" + salary +
                ", accountNo=" + accountNo +
                ", ifscCode=" + ifscCode +
                ", statements=" + statements +
                ", address=" + address +
                ", insertDt=" + insertDt +
                ", updateDt=" + updateDt +
                '}';
    }
}