package com.mine.ecomm.orderservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity(name = "ORDER_LINE_ITEMS")
@IdClass(OrderLineItemId.class)
public class OrderLineItem {
    @Id
    private String skuCode;
    @Id
    private String orderId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private String status;
    private String buyer;
    private String seller;

    @SuppressWarnings("JpaDataSourceORMInspection")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}