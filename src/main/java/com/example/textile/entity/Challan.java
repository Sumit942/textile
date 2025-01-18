package com.example.textile.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
public class Challan extends Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer challanNo;
    @ManyToOne
    private YarnOrderItemProduct yarnOrderItemProduct;
    private Double quantity;
    private String vehicleNo;
    @Temporal(TemporalType.DATE)
    private Date date;
    @ManyToOne
    private Company company;
    @OneToMany(mappedBy = "challan",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<FabricRoll> fabricRolls;
}
