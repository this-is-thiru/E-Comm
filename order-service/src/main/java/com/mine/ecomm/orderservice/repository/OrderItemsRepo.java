package com.mine.ecomm.orderservice.repository;

import java.util.List;

import com.mine.ecomm.orderservice.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mine.ecomm.orderservice.entity.OrderLineItem;
import com.mine.ecomm.orderservice.entity.OrderLineItemId;

public interface OrderItemsRepo extends JpaRepository<OrderLineItem, OrderLineItemId> {
    List<OrderLineItem> findByOrderEntity(OrderEntity orderEntity);
}
