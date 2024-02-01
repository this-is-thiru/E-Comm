package com.mine.ecomm.orderservice.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ORDER_ENTITY")
public class OrderEntity {
    @Id
    @Column(length = 50)
    private String orderId;
    @Column(length = 50)
    private String buyerEmail;
    private LocalDateTime orderedOn;

    // wait for these changes
    @OneToMany(mappedBy = "orderEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<OrderLineItem> orderLineItemList;
}