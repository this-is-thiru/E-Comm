package com.mine.ecomm.accountservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mine.ecomm.accountservice.entity.Seller;

public interface SellerRepository extends JpaRepository<Seller, String> {
//    List<Seller> findByPhoneNumber(String phoneNumber);
}
