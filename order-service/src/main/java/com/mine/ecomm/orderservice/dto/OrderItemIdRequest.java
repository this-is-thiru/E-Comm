package com.mine.ecomm.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemIdRequest {
    @Nonnull
    @JsonProperty("sku_code")
    private String skuCode;
    @Nonnull
    @JsonProperty("order_id")
    private String orderId;
}
