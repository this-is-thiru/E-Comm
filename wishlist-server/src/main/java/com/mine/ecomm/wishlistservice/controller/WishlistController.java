package com.mine.ecomm.wishlistservice.controller;

import com.mine.ecomm.wishlistservice.dto.ProductResponse;
import com.mine.ecomm.wishlistservice.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
