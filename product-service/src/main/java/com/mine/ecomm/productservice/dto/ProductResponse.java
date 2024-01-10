package com.mine.ecomm.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponse {
    private String productName;
    private double productPrice;
    private int discount;
    private double effectivePrice;
    private String shortDescription;
    private String description;
    private String category;
    private String sellerEmail;
    private String skuCode;
}
