package com.mine.ecomm.productservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mine.ecomm.productservice.entity.Product;
import org.springframework.data.mongodb.repository.Query;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByProductName(String productName);

    List<Product> searchProductByProductNameContainingIgnoreCase(String productName);

//    @Query("{'product_name': ?0,'product_seller_details.seller_email': ?1}")
    @Query("{$and: [{'product_name': ?0}, {'product_seller_details.seller_email': ?1}] }")
    Optional<Product> findByProductNameAndSellerEmail(String productName, String sellerEmail);

    @Query("{'sku_code': {$in: ?0}}")
    List<Product> findBySkuCodes(List<String> skuCodes);
}
