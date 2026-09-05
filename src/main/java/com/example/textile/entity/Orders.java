package com.example.textile.entity;

import com.example.textile.enums.OrderStatusType;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = false)
@Setter
@Getter
@Entity
public class Orders extends Document implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<CompanyYarnOrder> companyYarnOrders;
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
    @Enumerated(EnumType.STRING)
    private OrderStatusType orderStatusType = OrderStatusType.RECEIVED;
    private String remarks;
    @Column(unique = true)
    private String orderNo;
    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderProductMapping> orderProductMappings;
    //TODO: add User column (PrePersist)

    public void addCompanyYarnOrders(CompanyYarnOrder newYarnOrders) {
        if (Objects.isNull(this.companyYarnOrders)) {
            this.companyYarnOrders = new ArrayList<>();
        }
        newYarnOrders.setOrder(this);
        this.companyYarnOrders.add(newYarnOrders);

    }

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", orderStatusType=" + orderStatusType +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
