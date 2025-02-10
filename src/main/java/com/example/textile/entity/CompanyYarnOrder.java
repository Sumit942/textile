package com.example.textile.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
public class CompanyYarnOrder extends Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "companyYarnOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<YarnOrderItem> yarnOrderItems;
    @OneToMany(mappedBy = "companyYarnOrder", cascade = {CascadeType.PERSIST})
    private List<YarnBuilty> yarnBuilties;
    @ManyToOne
    private Orders order;
    @Temporal(TemporalType.DATE)
    private Date orderDt;
    private String yarnInvoiceNo;
    private String remark;
//    @NotNull
    private Double totalQuantity;
//    @NotNull
    private BigDecimal totalAmount;

    public void setYarnOrderItems(List<YarnOrderItem> yarnOrderItems) {
        if (Objects.nonNull(yarnOrderItems)) {
            for (YarnOrderItem yarnOrderItem : yarnOrderItems) {
                yarnOrderItem.setCompanyYarnOrder(this);
            }
        }
        this.yarnOrderItems = yarnOrderItems;
    }

    public void setYarnBuilties(List<YarnBuilty> yarnBuilties) {
        if (!CollectionUtils.isEmpty(yarnBuilties)) {
            for (YarnBuilty yarnBuilty : yarnBuilties) {
                yarnBuilty.setCompanyYarnOrder(this);
            }
        }
        this.yarnBuilties = yarnBuilties;
    }

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
