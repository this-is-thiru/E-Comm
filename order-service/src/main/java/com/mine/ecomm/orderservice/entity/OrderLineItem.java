package com.mine.ecomm.orderservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "ORDER_LINE_ITEMS")
@IdClass(OrderLineItemId.class)
public class OrderLineItem {
    @Id
    private String skuCode;
    @Id
    private String orderNumber;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private String status;
    private String seller;
    private LocalDateTime orderedOn;
    private LocalDateTime deliveredOn;
    private LocalDateTime cancelledOn;
    private LocalDateTime returnedOn;

    @SuppressWarnings("JpaDataSourceORMInspection")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity orderEntity;
}