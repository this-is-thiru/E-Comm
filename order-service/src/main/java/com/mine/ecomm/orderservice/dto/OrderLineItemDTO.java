package com.mine.ecomm.orderservice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemDTO {
    private String skuCode;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private OrderStatus status;
    private String sellerEmail;
    private LocalDateTime orderedOn;
    private LocalDateTime deliveredOn;
    private LocalDateTime cancelledOn;
    private LocalDateTime returnedOn;
}