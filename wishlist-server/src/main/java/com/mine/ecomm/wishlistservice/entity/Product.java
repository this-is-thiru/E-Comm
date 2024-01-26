package com.mine.ecomm.wishlistservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "WISHLIST_PRODUCTS")
public class Product {
    @Id
    private String buyerEmail;
    private String productId;
    private String productImageUrl;
    private String productName;
    private Double markedPrice;
    private Double productPrice;
    private Integer discount;
    private Double rating;
}
