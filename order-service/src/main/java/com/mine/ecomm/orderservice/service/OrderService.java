package com.mine.ecomm.orderservice.service;

import com.mine.ecomm.orderservice.dto.OrderDTO;
import com.mine.ecomm.orderservice.dto.OrderStatus;
import com.mine.ecomm.orderservice.entity.Order;
import com.mine.ecomm.orderservice.exception.ServiceException;
import com.mine.ecomm.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplateProvider restTemplateProvider;

    public List<OrderDTO> getAllBuyerOrders(final String buyerEmail) {
        List<OrderDTO> buyerOrders = new ArrayList<>();
        List<Order> allOrders = orderRepository.findByBuyer(buyerEmail);
        for (Order order : allOrders) {
            buyerOrders.add(new OrderDTO(order));
        }
        return buyerOrders;
    }

    public String cancelOrderById(final int id) {
        Optional<Order> optOrder = orderRepository.findById(id);
        if (optOrder.isEmpty()) {
            throw new ServiceException("Order is not present with id.");
        }
        final Order order = optOrder.get();
        order.setStatus(OrderStatus.CANCELLED.getVal());
        orderRepository.saveAndFlush(order);
        return "Your order has been successfully cancelled.";
    }

    public String returnOrderById(final int id) {
        Optional<Order> optOrder = orderRepository.findById(id);
        if (optOrder.isEmpty()) {
            throw new ServiceException("Order is not present with id.");
        }
        final Order order = optOrder.get();
        order.setStatus(OrderStatus.RETURNED.getVal());
        orderRepository.saveAndFlush(order);
        return "Return request has been created successfully.";
    }

    public boolean rateSeller(final String seller, final int rate) {
        final ResponseEntity<String> response = restTemplateProvider.post(seller, rate);
        return response.getStatusCode() == HttpStatus.OK;
    }
}