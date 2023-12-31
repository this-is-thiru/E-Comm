package com.mine.ecomm.sellerservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class SellerRating {
    @Id
    private String sellerEmail;
    private float rating;
}
