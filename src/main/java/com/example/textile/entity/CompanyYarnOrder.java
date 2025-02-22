package com.example.textile.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = false)
@Setter
@Getter
@Entity
public class CompanyYarnOrder extends Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "companyYarnOrder", cascade = CascadeType.ALL)
    private List<YarnOrderItem> yarnOrderItems;
    @OneToMany(mappedBy = "companyYarnOrder", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference
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
    private Double cGst;
    private Double sGst;
    private Double iGst;

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

    public void addYarnBuilty(YarnBuilty yarnBuilty) {
        if (Objects.isNull(this.yarnBuilties)) {
            this.yarnBuilties = new ArrayList<>();
        }
        yarnBuilty.setCompanyYarnOrder(this);
        this.yarnBuilties.add(yarnBuilty);
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

    @Override
    public String toString() {
        return "CompanyYarnOrder{" +
                "id=" + id +
                ", orderId=" + (order !=null ? order.getId() : null) +
                '}';
    }
}
