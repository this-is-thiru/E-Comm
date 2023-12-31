package com.mine.ecomm.sellerservice.dto;

import java.util.List;

import lombok.Data;

@Data
public class SellerDTO {
    private String productName;
    private double productPrice;
    private int discount;
    private String shortDescription;
    private String description;
    private String category;
    private long itemsSold;
    private String sellerEmail;
    private Double earnFromProduct;
    private static final List<String> categories = List.of("mobile", "television", "camera", "headphone", "watch", "laptop");
}
