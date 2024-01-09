package com.mine.ecomm.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductSellerDetail {

    /** The Product name. */
    private double productPrice;

    /** The Discount. */
    private int discount;

    /** The Seller email. */
    private String sellerEmail;

    private Long quantity;
}
