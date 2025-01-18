package com.example.textile.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
public class CompanyYarnOrder extends Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "companyYarnOrder", cascade = {CascadeType.ALL})
    private List<YarnOrderItem> yarnOrderItems;
    @OneToMany(mappedBy = "companyYarnOrder")
    private List<YarnBuilty> yarnBuilties;
    @ManyToOne
    private Orders orders;
    @Temporal(TemporalType.DATE)
    private LocalDate orderDt;
    private String yarnInvoiceNo;
    private String remark;


    /*public String getYarnFabricDesign() {
        String yarns = "NA";
        if (Objects.nonNull(yarnOrderItems) && !yarnOrderItems.isEmpty()) {
            yarns = yarnOrderItems.stream().map(YarnOrderItem::getYarn).map(Yarn::getType).collect(Collectors.joining(" x "));
        }
        return yarns
                .concat(" - ")
                .concat(Objects.nonNull(fabricDesign) ? fabricDesign.getName() : "NA")
                .concat(Objects.nonNull(gsm) ? " | ".concat(gsm).concat(" GSM") : "");
    }*/
}
