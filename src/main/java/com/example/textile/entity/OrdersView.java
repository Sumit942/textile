package com.example.textile.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Immutable
@Getter
@Setter
@ToString
public class OrdersView {
    @Id
    private Long id;
    private Long orderId;
    private String orderStatusType;
    private Long companyId;
    private String itemsWithQuantities;
    private String orderNo;
}
