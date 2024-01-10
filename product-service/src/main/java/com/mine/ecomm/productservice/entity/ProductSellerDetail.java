package com.mine.ecomm.productservice.entity;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductSellerDetail {

    @Field("product_price")
    private Double productPrice;

    @Field("discount")
    private Integer discount;

    @Field("effective_price")
    private Double effectivePrice;

    @Field("seller_email")
    private String sellerEmail;

    @Field("quantity")
    private Long quantity;
}
