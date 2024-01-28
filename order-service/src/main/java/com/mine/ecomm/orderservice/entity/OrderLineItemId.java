package com.mine.ecomm.orderservice.entity;

import java.io.Serializable;

import jakarta.annotation.Nonnull;
import lombok.Getter;

@Getter
public class OrderLineItemId implements Serializable {
    @Nonnull
    private final String skuCode;
    @Nonnull
    private final String orderEntity;

    public OrderLineItemId(@Nonnull String skuCode, @Nonnull String orderId) {
        this.skuCode = skuCode;
        this.orderEntity = orderId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OrderLineItemId other = (OrderLineItemId) obj;
        if (!orderEntity.equals(other.orderEntity))
            return false;
        return skuCode.equals(other.skuCode);
    }
}
