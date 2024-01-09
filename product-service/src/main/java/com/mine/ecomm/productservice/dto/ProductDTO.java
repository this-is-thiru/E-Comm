package com.mine.ecomm.productservice.dto;

import java.util.List;

import com.mine.ecomm.productservice.entity.Product;
import com.mine.ecomm.productservice.entity.ProductSellerDetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {

    /** The Product name. */
    private String productName;

    /** The Product price. */
    private double productPrice;

    /** The Discount. */
    private int discount;

    /** The Short description. */
    private String shortDescription;

    /** The Description. */
    private String description;

    /** The Category. */
    private String category;

    /** The Price detail ids. */
    private long itemsSold;

    /** The Seller email. */
    private String sellerEmail;

    /** The Product id. */
    private String productId;

    /** The Price details. */
    private List<ProductSellerDetail> priceDetails;

    private Long quantity;

    /** The Categories. */
    private static final List<String> categories = List.of("mobile", "television", "camera", "headphone", "watch", "laptop");

    public ProductDTO(final Product product) {
        this.productName = product.getProductName();
        this.shortDescription = product.getShortDescription();
        this.description = product.getDescription();
        this.category = product.getCategory();
    }
}
