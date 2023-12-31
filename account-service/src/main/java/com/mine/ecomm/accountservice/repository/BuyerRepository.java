package com.mine.ecomm.accountservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mine.ecomm.accountservice.entity.Buyer;

public interface BuyerRepository extends JpaRepository<Buyer, String> {
    List<Buyer> findByPhoneNumber(String phoneNumber);
}
