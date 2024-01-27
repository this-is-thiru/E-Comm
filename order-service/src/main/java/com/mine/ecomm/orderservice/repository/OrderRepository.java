package com.mine.ecomm.orderservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mine.ecomm.orderservice.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    List<OrderEntity> findByBuyer(String buyer);
}
