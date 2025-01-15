package com.example.textile.entity;

import com.example.textile.enums.OrderStatusType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
public class Orders extends Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "orders")
    private List<YarnOrder> yarnOrders;
    @ManyToOne
    private Company company;
    @Enumerated(EnumType.STRING)
    private OrderStatusType orderStatusType = OrderStatusType.RECEIVED;
    private String remarks;

}
