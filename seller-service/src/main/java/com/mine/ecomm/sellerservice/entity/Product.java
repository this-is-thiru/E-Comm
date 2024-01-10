package com.mine.ecomm.sellerservice.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

/**
 * The type Seller catalog.
 */
@Entity(name = "SELLER_CATALOGS")
@Data
public class Product {

    /** Product id */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String productId;

    /** Product name */
    private String productName;

    /** Product price */
    private double productPrice;

    /** Discount on product price */
    private int discount;

    /** Short description of product */
    private String shortDescription;

    /** Description of product */
    private String description;

    /** Product category */
    private String category;

    /** Seller email id */
    private String sellerEmail;

    /** The Total products. */
    private long totalProducts;

    /** The In transit products. */
    private long inTransitProducts;

    /** The Items sold. */
    private long itemsSold;

    /** The Earn from product. */
    private Double earnFromProduct;

    /** The Image. */
    private String image;

    /** The Open. */
    private long open;

    /** The Canceled. */
    private long canceled;

    /** The Delivered. */
    private long delivered;

    /** The Returned. */
    private long returned;

    /** The Rating. */
    private int rating;

    /** The Cart ids. */
    @ElementCollection
    private List<String> cartIds;

    /** Categories of products to sell */
    private static final List<String> categories = List.of("mobile", "television", "camera", "headphone", "watch", "laptop");

    public Product(){
        this.cartIds = new ArrayList<>();
    }

    public void addCartId(String cartId) {
        this.cartIds.add(cartId);
    }
}
