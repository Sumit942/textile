package com.example.textile.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @ManyToOne
    private FabricDesign fabricDesign;
    private String gsm;
    @Transient
    String yarnFabricDesign;

    public String getYarnFabricDesign() {
        String yarns = "NA";
        if (Objects.nonNull(yarnOrderItems) && !yarnOrderItems.isEmpty()) {
            yarns = yarnOrderItems.stream().map(YarnOrderItem::getYarn).map(Yarn::getType).collect(Collectors.joining(" x "));
        }
        return yarns
                .concat(" - ")
                .concat(Objects.nonNull(fabricDesign) ? fabricDesign.getDesign() : "NA")
                .concat(Objects.nonNull(gsm) ? " | ".concat(gsm).concat(" GSM") : "");
    }
}
