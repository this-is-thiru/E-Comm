package com.mine.ecomm.orderservice.dto;

import com.mine.ecomm.orderservice.entity.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class OrderDTO {
    private int id;
    private String orderId;
    private String productId;
    private LocalDateTime orderedOn;
    private LocalDateTime deliveredOn;
    private LocalDateTime returnedOn;
    private LocalDateTime cancelledOn;
    private OrderStatus status;
    private String buyer;
    private String seller;

    public OrderDTO(final Order order){
        this.id = order.getId();
        this.orderId = order.getOrderId();
        this.productId = order.getProductId();
        this.orderedOn = order.getOrderedOn();
        this.deliveredOn = order.getDeliveredOn();
        this.returnedOn = order.getReturnedOn();
        this.cancelledOn = order.getCancelledOn();
        this.status = OrderStatus.valToStatus(order.getStatus());
        this.buyer = order.getBuyer();
        this.seller = order.getSeller();
    }
}