package com.mine.ecomm.productservice.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "product")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Product {

    /** The id. */
    @Id
    private String id;

    /** The Product name. */
    private String productName;

    /** The Short description. */
    private String shortDescription;

    /** The Description. */
    private String description;

    /** The Category. */
    private String category;

    /** The Product price. */
    private double productPrice;

    /** The Discount. */
    private int discount;

    /** The Seller email. */
    private String sellerEmail;

    /** The Price detail ids. */
    private List<String> priceDetailIds;

    /** The Categories. */
    private static final List<String> categories = List.of("mobile", "television", "camera", "headphone", "watch", "laptop");

    /**
     * Add price detail id.
     *
     * @param priceDetailId the price detail id
     */
    public void addPriceDetailId(String priceDetailId) {
        if (priceDetailIds == null){
            priceDetailIds = new ArrayList<>();
        }
        this.priceDetailIds.add(priceDetailId);
    }
}
