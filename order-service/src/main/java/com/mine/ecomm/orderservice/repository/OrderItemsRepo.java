package com.mine.ecomm.orderservice.repository;

import com.mine.ecomm.orderservice.entity.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepo extends JpaRepository<OrderLineItem, Integer> {
}
