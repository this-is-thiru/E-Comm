package com.mine.ecomm.sellerservice.dto;

import java.util.List;

import com.mine.ecomm.sellerservice.entity.Product;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Product dto.
 */
@Data
@NoArgsConstructor
public class ProductDTO {
    private String productId;
    private String productName;
    private double productPrice;
    private int discount;
    private String shortDescription;
    private String description;
    private String category;
    private long itemsSold;
    private String sellerEmail;
    private Double earnFromProduct;
    private long totalProducts;
    private long inTransitProducts;
    private String image;
    private List<String> cartIds;

    public ProductDTO(final Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.productPrice = product.getProductPrice();
        this.discount = product.getDiscount();
        this.shortDescription = product.getShortDescription();
        this.description = product.getDescription();
        this.category = product.getCategory();
        this.sellerEmail = product.getSellerEmail();
        this.itemsSold = product.getItemsSold();
        this.earnFromProduct = product.getEarnFromProduct();
        this.totalProducts = product.getTotalProducts();
        this.inTransitProducts = product.getInTransitProducts();
        this.image = product.getImage();
        this.cartIds = product.getCartIds();
    }

    private static final List<String> categories = List.of("mobile", "television", "camera", "headphone", "watch", "laptop");
}
