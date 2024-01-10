package com.mine.ecomm.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductSellerDetail {

    @Field("product_price")
    private double productPrice;

    @Field("discount")
    private int discount;

    @Field("effective_price")
    private double effectivePrice;

    @Field("seller_email")
    private String sellerEmail;

    @Field("quantity")
    private Long quantity;
}
