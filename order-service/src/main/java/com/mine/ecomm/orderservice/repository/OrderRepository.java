package com.mine.ecomm.orderservice.repository;

import com.mine.ecomm.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    public List<Order> findByBuyer(String buyer);
}
