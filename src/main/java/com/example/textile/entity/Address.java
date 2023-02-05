package com.example.textile.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Address implements Serializable {
    private Long id;
    private State state;
    private String address;
    private Integer pinCode;
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

    @ManyToOne
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @NotNull
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPinCode() {
        return pinCode;
    }

    public void setPinCode(Integer pinCode) {
        this.pinCode = pinCode;
    }

    @CreationTimestamp
    @Column(updatable = false)
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

    @Transient
    public String getFullAddress() {
        return address + (state != null ? ","+state.getName() : "")  + "-" + pinCode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", state=" + state +
                ", address='" + address + '\'' +
                ", pinCode=" + pinCode +
                '}';
    }
}
