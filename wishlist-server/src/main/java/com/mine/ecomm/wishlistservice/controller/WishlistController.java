package com.mine.ecomm.wishlistservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.mine.ecomm.wishlistservice.dto.ProductResponse;
import com.mine.ecomm.wishlistservice.entity.ProductId;
import com.mine.ecomm.wishlistservice.exception.WishlistServiceException;
import com.mine.ecomm.wishlistservice.service.WishlistService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wishlist/{buyer-email}")
public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public String addProductToWishlist(final @PathVariable("buyer-email") String buyerEmail, final @PathVariable String productId) {
        return wishlistService.addProductToWishlist(buyerEmail, productId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllWishlistProducts(final @PathVariable("buyer-email") String buyerEmail) {
        return wishlistService.getAllWishlistProducts(buyerEmail);
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteProductFromWishlist(final @PathVariable("buyer-email") String buyerEmail, final @PathVariable String productId) {
        final ProductId productIdObj = new ProductId(productId, buyerEmail);
        final Boolean isRemoved = wishlistService.deleteProductFromWishlist(productIdObj);
        if (!isRemoved) {
            throw new WishlistServiceException("Product not found in wishlist");
        }
        return "Product removed from wishlist";
    }
}
