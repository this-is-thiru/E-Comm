package com.mine.ecomm.sellerservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mine.ecomm.sellerservice.entity.Product;

public interface SellerCatalogRepository extends JpaRepository<Product, String> {

    List<Product> findByProductName(String productName);
    List<Product> findBySellerEmail(String sellerEmail);
}
