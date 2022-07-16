package com.example.textile.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table
public class Invoice implements Serializable {

    @Id
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private TransportMode transportMode;
    private Date invoiceDate;
    private String vehicleNo;
    private Boolean reverseCharge = Boolean.FALSE;
    private Date dateOfSupply;
    private String state;
    private Integer code;
    private String placeOfSupply;
    @ManyToOne
    private Company billToParty;
    @ManyToOne
    private Company shipToParty;

}
