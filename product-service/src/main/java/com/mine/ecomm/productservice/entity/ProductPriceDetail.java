package com.mine.ecomm.productservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "product_price_detail")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductPriceDetail {

    /** The id. */
    @Id
    private String id;

    /** The Product name. */
    private double productPrice;

    /** The Discount. */
    private int discount;

    /** The Seller email. */
    private String sellerEmail;

}
