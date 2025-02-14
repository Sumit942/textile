package com.example.textile.entity;

import com.example.textile.enums.OrderStatusType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@ToString(exclude = {"companyYarnOrders"})
public class Orders extends Document implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private List<CompanyYarnOrder> companyYarnOrders;
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
    @Enumerated(EnumType.STRING)
    private OrderStatusType orderStatusType = OrderStatusType.RECEIVED;
    private String remarks;
    //TODO: add User column (PrePersist)

    public void addCompanyYarnOrders(CompanyYarnOrder newYarnOrders) {
        if (Objects.isNull(this.companyYarnOrders)) {
            this.companyYarnOrders = new ArrayList<>();
        }
        newYarnOrders.setOrder(this);
        this.companyYarnOrders.add(newYarnOrders);

    }
    /*public void setCompanyYarnOrders(List<CompanyYarnOrder> companyYarnOrders) {
        if (Objects.nonNull(companyYarnOrders)) {
            for (CompanyYarnOrder companyYarnOrder : companyYarnOrders) {
                companyYarnOrder.setOrder(this);
            }
        }

        this.companyYarnOrders = companyYarnOrders;
    }

    public void addCompanyYarnOrders(List<CompanyYarnOrder> newYarnOrders) {
        if (Objects.nonNull(newYarnOrders)) {
            for (CompanyYarnOrder newYarnOrder : newYarnOrders) {
                newYarnOrder.setOrder(this);
            }
        }
        if (Objects.isNull(this.companyYarnOrders)) {
            this.companyYarnOrders = newYarnOrders;
        } else {
            this.companyYarnOrders.addAll(newYarnOrders);
        }
    }*/
}
