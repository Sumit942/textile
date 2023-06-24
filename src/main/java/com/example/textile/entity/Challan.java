package com.example.textile.entity;

import com.example.textile.enums.SaleTypeEnum;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Challan {
    private Long id;
    private Date chDate;
    private String fullChallanNo;
    private String challanNo;
    private String type;
    private Company partyName;
    private String transportName;
    private List<Yarn> yarn;
    private String gsm;
    private Machine machine;
    private String finishDia;
    private Double quantity;
    private List<Roll> rolls;

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

    @Transient
    public String getFullChallanNo() {
        return SaleTypeEnum.FJW.getChallanFormat() + challanNo;
    }

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @ManyToOne
    public Company getPartyName() {
        return partyName;
    }

    public void setPartyName(Company partyName) {
        this.partyName = partyName;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    @OneToMany(mappedBy = "id")
    public List<Yarn> getYarn() {
        return yarn;
    }

    public void setYarn(List<Yarn> yarn) {
        this.yarn = yarn;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    @ManyToOne
    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public String getFinishDia() {
        return finishDia;
    }

    public void setFinishDia(String finishDia) {
        this.finishDia = finishDia;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @OneToMany
    public List<Roll> getRolls() {
        return rolls;
    }

    public void setRolls(List<Roll> rolls) {
        this.rolls = rolls;
    }

    @Temporal(TemporalType.DATE)
    public Date getChDate() {
        return chDate;
    }

    public void setChDate(Date chDate) {
        this.chDate = chDate;
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
}
