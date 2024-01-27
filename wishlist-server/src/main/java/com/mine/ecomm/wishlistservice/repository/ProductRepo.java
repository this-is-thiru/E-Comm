package com.mine.ecomm.wishlistservice.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mine.ecomm.wishlistservice.entity.Product;
import com.mine.ecomm.wishlistservice.entity.ProductId;

public interface ProductRepo extends JpaRepository<Product, ProductId> {
    Set<Product> findByBuyerEmail(String buyerEmail);
}
