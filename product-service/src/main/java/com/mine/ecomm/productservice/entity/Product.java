package com.mine.ecomm.productservice.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(value = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {

    @MongoId
    private String id;

    @Field("sku_code")
    private String skuCode;

    @Field("product_name")
    private String productName;

    @Field("short_description")
    private String shortDescription;

    @Field("description")
    private String description;

    @Field("category")
    private String category;

    @Field("quantity")
    private Long quantity;

    @Field("product_seller_details")
    private List<ProductSellerDetail> productSellerDetails;

    private static final List<String> categories = List.of("mobile", "television", "camera", "headphone", "watch", "laptop");
}
