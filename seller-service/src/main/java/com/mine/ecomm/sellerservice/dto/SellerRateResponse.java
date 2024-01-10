package com.mine.ecomm.sellerservice.dto;

import lombok.Data;

@Data
public class SellerRateResponse {
    private String sellerEmail;
    private String sellerName;
    private Float rating;
    private Integer deliveryCharge;
}
