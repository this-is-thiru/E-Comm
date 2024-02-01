package com.mine.ecomm.orderservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "ORDER_LINE_ITEMS")
@IdClass(OrderLineItemId.class)
public class OrderLineItem {
    @Id
    @Column(length = 50)
    private String skuCode;
    @Column(length = 50)
    private String productName;
    @Column(length = 10)
    private String status;
    @Column(length = 20)
    private String buyerEmail;
    @Column(length = 20)
    private String sellerEmail;
    private Double productPrice;
    private Integer quantity;
    private LocalDateTime orderedOn;
    private LocalDateTime deliveredOn;
    private LocalDateTime cancelledOn;
    private LocalDateTime returnedOn;

    @SuppressWarnings("JpaDataSourceORMInspection")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Id
    @JoinColumn(name = "fk_order_id", nullable = false)
    private OrderEntity orderEntity;
}