package com.mine.ecomm.productservice.dto;


import lombok.Data;

@Data
public class SellerDetail {
    private Double productPrice;
    private Integer discount;
    private Double effectivePrice;
    private Float rating;
    private Integer deliveryCharge;
    private String sellerName;
    private String sellerEmail;
}
