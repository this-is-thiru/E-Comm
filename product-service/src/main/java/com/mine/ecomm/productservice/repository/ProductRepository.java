package com.mine.ecomm.productservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mine.ecomm.productservice.entity.Product;
import org.springframework.data.mongodb.repository.Query;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByProductName(String productName);


//    @Query("{'productName': ?0,'product_seller_details.sellerEmail': ?1}")
    @Query("{$and: [{'productName': ?0}, {'product_seller_details.sellerEmail': ?1}] }")
    Optional<Product> findByProductNameAndSellerEmail(String productName, String sellerEmail);
}
