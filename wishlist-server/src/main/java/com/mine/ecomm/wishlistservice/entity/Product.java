package com.mine.ecomm.wishlistservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "WISHLIST_PRODUCTS")
@IdClass(ProductId.class)
public class Product {
    @Id
    private String productId;
    @Id
    private String buyerEmail;
    private String productImageUrl;
    private String productName;
    private Double markedPrice;
    private Double productPrice;
    private Integer discount;
    private Double rating;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wishlist_id", nullable = false)
    @SuppressWarnings("JpaDataSourceORMInspection")
    private Wishlist wishlist;
}
