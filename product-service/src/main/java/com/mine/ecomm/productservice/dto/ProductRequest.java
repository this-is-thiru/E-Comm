package com.mine.ecomm.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRequest {
    private String productName;
    private double productPrice;
    private int discount;
    private String shortDescription;
    private String description;
    private String category;
    private String sellerEmail;
    private Long quantity;
}
