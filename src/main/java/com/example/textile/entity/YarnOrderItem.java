package com.example.textile.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Setter
@Getter
public class YarnOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Yarn yarn;
    @ManyToOne
    @JsonIgnore
    private CompanyYarnOrder companyYarnOrder;
    @Column(nullable = false)
    private Double quantity;
    private Integer boxes;
    private Double rate;
    private String hsn;
    private String lotNo;
    private BigDecimal amount;
    @ManyToMany
    @JoinTable(
            name = "yarn_order_item_product_mapping",
            joinColumns = @JoinColumn(name = "yarn_order_item_id"),
            inverseJoinColumns = @JoinColumn(name = "yarn_order_item_product_id")
    )
    @JsonManagedReference
    private List<YarnOrderItemProduct> yarnOrderItemProducts;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        YarnOrderItem that = (YarnOrderItem) o;
        return Objects.equals(id, that.id) && Objects.equals(quantity, that.quantity) && Objects.equals(boxes, that.boxes) && Objects.equals(rate, that.rate) && Objects.equals(hsn, that.hsn) && Objects.equals(lotNo, that.lotNo) && Objects.equals(amount, that.amount) &&
                (Objects.nonNull(that.getYarn()) && Objects.equals(yarn.getId(), that.yarn.getId())) &&
                (Objects.nonNull(that.getCompanyYarnOrder()) && Objects.equals(companyYarnOrder.getId(), that.companyYarnOrder.getId()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Objects.nonNull(yarn) ? yarn.getId() : null, Objects.nonNull(companyYarnOrder) ? companyYarnOrder.getId() : null, quantity, boxes, rate, hsn, lotNo, amount);
    }
}
