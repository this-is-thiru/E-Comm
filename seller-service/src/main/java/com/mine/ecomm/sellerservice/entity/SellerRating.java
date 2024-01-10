package com.mine.ecomm.sellerservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "SELLERS_RATING")
@Data
public class SellerRating {
    @Id
    private String sellerEmail;
    private String sellerName;
    private Integer deliveryCharge;
    private float rating;
}
