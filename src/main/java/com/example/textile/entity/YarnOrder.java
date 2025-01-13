package com.example.textile.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
public class YarnOrder extends Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "yarnOrder")
    private List<YarnOrderItem> yarnOrderItems;
    @ManyToOne
    private Orders orders;
    private Double loadUnloadCharges;
    private String vehicleNo;
    @ManyToOne
    private Company transport;
}
