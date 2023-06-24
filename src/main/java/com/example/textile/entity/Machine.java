package com.example.textile.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Machine {
    private Long id;
    private String machineNo;
    private int dia;
    private int guage;
    private int feeder;
    private String type;
    private String details;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getGuage() {
        return guage;
    }

    public void setGuage(int guage) {
        this.guage = guage;
    }

    public int getFeeder() {
        return feeder;
    }

    public void setFeeder(int feeder) {
        this.feeder = feeder;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
