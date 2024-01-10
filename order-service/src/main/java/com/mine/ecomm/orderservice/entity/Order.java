package com.mine.ecomm.orderservice.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String orderId;
    private String productId;
    private LocalDateTime orderedOn;
    private LocalDateTime deliveredOn;
    private LocalDateTime returnedOn;
    private LocalDateTime cancelledOn;
    private String status;
    private String buyer;
    private String seller;

    // wait for these changes
    private String orderNumber;
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderLineItems> orderLineItemsList;
}