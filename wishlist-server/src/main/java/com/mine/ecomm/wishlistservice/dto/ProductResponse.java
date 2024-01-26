package com.mine.ecomm.wishlistservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class ProductResponse {
    private String skuCode;
    private String productName;
    private String productImageUrl;
    private double productPrice;
    private int discount;
    private double effectivePrice;
    private double rating;
}
