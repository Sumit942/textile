package com.example.textile.entity;

import javax.persistence.*;

@Entity
public class Roll {
    private Long id;
    private Double weight;
    private int rollNo;
    private FabricDesign fabricDesign;
    private boolean isRejected = false;
    private Machine machine;
    private Challan roll;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Column
    public int getRollNo() {
        return rollNo;
    }

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    @ManyToOne
    public FabricDesign getFabricDesign() {
        return fabricDesign;
    }

    public void setFabricDesign(FabricDesign fabricDesign) {
        this.fabricDesign = fabricDesign;
    }

    @Column
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

    @ManyToOne
    public Challan getRoll() {
        return roll;
    }

    public void setRoll(Challan roll) {
        this.roll = roll;
    }
}
