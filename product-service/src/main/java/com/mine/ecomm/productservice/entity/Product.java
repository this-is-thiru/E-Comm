package com.mine.ecomm.productservice.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {

    /** The id. */
    @Id
    @Field("_id")
    private String id;

    private String skuCode;

    /** The Product name. */
    private String productName;

    /** The Short description. */
    private String shortDescription;

    /** The Description. */
    private String description;

    /** The Category. */
    private String category;

    private Long quantity;

    @Field("product_seller_details")
    private List<ProductSellerDetail> productSellerDetails;

    /** The Categories. */
    private static final List<String> categories = List.of("mobile", "television", "camera", "headphone", "watch", "laptop");
}
