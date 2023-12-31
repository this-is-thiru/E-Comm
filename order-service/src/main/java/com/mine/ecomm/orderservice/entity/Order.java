package com.mine.ecomm.orderservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity(name="Orders")
@Data
public class Order {
    @Id
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
}