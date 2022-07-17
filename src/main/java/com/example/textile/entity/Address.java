package com.example.textile.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Address implements Serializable {
    private Long id;
    private State state;
    private String address;
    private Integer pinCode;

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
