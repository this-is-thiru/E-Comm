package com.mine.ecomm.orderservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mine.ecomm.orderservice.dto.OrderRequest;
import com.mine.ecomm.orderservice.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    /**
     * Test Path variable and path param.
     *
     * @param seller the seller
     * @param rating the rating
     * @return the response entity
     */
    @GetMapping("/rate/{seller}")
    public ResponseEntity<String> test(final @PathVariable String seller, final @RequestParam("rate") int rating) {
        final String message = seller + rating;
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/all/buyer/{email}")
    public ResponseEntity<List<OrderRequest>> getAllBuyerOrders(final @PathVariable String email) {
        final List<OrderRequest> buyerOrders = orderService.getAllBuyerOrders(email);
        return new ResponseEntity<>(buyerOrders, HttpStatus.OK);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrderById(final @PathVariable int id) {
        final String msg = orderService.cancelOrderById(id);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @PostMapping("/return/{id}")
    public ResponseEntity<String> returnOrderById(final @PathVariable int id) {
        final String msg = orderService.returnOrderById(id);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }

    @PostMapping("/seller/rate/{seller}")
    public ResponseEntity<String> rateTheSeller(final @PathVariable String seller, final @RequestParam int rate) {
        final boolean isSuccess = orderService.rateSeller(seller, rate);
        final String response = isSuccess ? "Rating successfully noted." : "Rating not noted.";
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
