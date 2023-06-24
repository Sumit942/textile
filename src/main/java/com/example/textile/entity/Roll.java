package com.example.textile.entity;

import javax.persistence.*;

@Entity
public class Roll {
    private Long id;
    private Double weight;
    private int rollNo;
    private String type;
    private boolean isRejected = false;
    private Machine machine;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    @ManyToOne
    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }
}
