package com.mine.ecomm.sellerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mine.ecomm.sellerservice.entity.SellerRating;

public interface SellerRateRepository extends JpaRepository<SellerRating, String> {
}
