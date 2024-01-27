package com.mine.ecomm.wishlistservice.entity;

import java.io.Serializable;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ProductId implements Serializable {
    @Nonnull
    private String productId;
    @Nonnull
    private String buyerEmail;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ProductId other = (ProductId) obj;
        if (!buyerEmail.equals(other.buyerEmail)) {
            return false;
        }
        return productId.equals(other.productId);
    }
}
