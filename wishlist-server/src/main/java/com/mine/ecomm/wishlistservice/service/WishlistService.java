package com.mine.ecomm.wishlistservice.service;

import com.mine.ecomm.wishlistservice.entity.Wishlist;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.mine.ecomm.wishlistservice.dto.ProductResponse;
import com.mine.ecomm.wishlistservice.entity.Product;
import com.mine.ecomm.wishlistservice.repository.WishlistRepo;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepo wishlistRepo;
    private final WebClient webClient;

    public String addProductToWishlist(final String buyerEmail, final String  productId) {
        final ProductResponse productResponse = this.getProductDetails(productId);
        final Product product = convertProductResponseToProduct(productResponse);
        product.setBuyerEmail(buyerEmail);
        final Wishlist wishlist = new Wishlist();
        wishlist.setBuyerEmail(buyerEmail);
        wishlist.getProducts().add(product);
        wishlistRepo.save(wishlist);
        return "Product added to wishlist";
    }

    public List<ProductResponse> getAllWishlistProducts(final String buyerEmail) {
        final Optional<Wishlist> optionalWishlist = wishlistRepo.findById(buyerEmail);
        if (optionalWishlist.isPresent()) {
            final Wishlist wishlist = new Wishlist();
            final List<Product> products = wishlist.getProducts();
            return products.stream().map(this::convertProductToProductResponse).toList();
        }
        return List.of();
    }

    private ProductResponse getProductDetails(final String  productId) {
        return webClient.post()
                .uri("http://localhost:8083/api/product/{sku-code}", productId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ClientResponse::createException)
                .bodyToMono(ProductResponse.class)
                .block();
    }

    private Product convertProductResponseToProduct(final ProductResponse productResponse) {
        return Product.builder()
                .productId(productResponse.getSkuCode())
                .productName(productResponse.getProductName())
                .productImageUrl(productResponse.getProductImageUrl())
                .markedPrice(productResponse.getProductPrice())
                .discount(productResponse.getDiscount())
                .productPrice(productResponse.getEffectivePrice())
                .rating(productResponse.getRating())
                .build();
    }

    private ProductResponse convertProductToProductResponse(final Product product) {
        return ProductResponse.builder()
                .skuCode(product.getProductId())
                .productName(product.getProductName())
                .productImageUrl(product.getProductImageUrl())
                .productPrice(product.getMarkedPrice())
                .discount(product.getDiscount())
                .effectivePrice(product.getProductPrice())
                .rating(product.getRating())
                .build();
    }
}
