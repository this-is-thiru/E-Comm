package com.mine.ecomm.orderservice.dto;

import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public enum OrderStatus {
    OPEN("open"),
    DELIVERED("delivered"),
    CANCELLED("cancelled"),
    RETURNED("returned");

    private final String val;

    OrderStatus(String val) {
        this.val = val;
    }

    public static OrderStatus valToStatus(@NonNull final String val) {
        return switch (val) {
            case "open" -> OPEN;
            case "delivered" -> DELIVERED;
            case "cancelled" -> CANCELLED;
            case "returned" -> RETURNED;
            default -> null;
        };
    }
}
