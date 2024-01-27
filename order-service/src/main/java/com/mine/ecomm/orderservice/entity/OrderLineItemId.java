package com.mine.ecomm.orderservice.entity;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderLineItemId implements Serializable {
    @Nonnull
    private String skuCode;
    @Nonnull
    private String orderNumber;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OrderLineItemId other = (OrderLineItemId) obj;
        if (!orderNumber.equals(other.orderNumber))
            return false;
        return skuCode.equals(other.skuCode);
    }
}
