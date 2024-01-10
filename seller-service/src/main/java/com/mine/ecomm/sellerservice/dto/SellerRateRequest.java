package com.mine.ecomm.sellerservice.dto;

import lombok.Data;

@Data
public class SellerRateRequest {
    private String sellerEmail;
    private Float rating;
    private String sellerName;
}
