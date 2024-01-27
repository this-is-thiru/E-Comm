package com.mine.ecomm.wishlistservice.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.mine.ecomm.wishlistservice.dto.ProductResponse;
import com.mine.ecomm.wishlistservice.entity.Product;
import com.mine.ecomm.wishlistservice.entity.ProductId;
import com.mine.ecomm.wishlistservice.entity.Wishlist;
import com.mine.ecomm.wishlistservice.repository.ProductRepo;
import com.mine.ecomm.wishlistservice.repository.WishlistRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepo wishlistRepo;
    private final ProductRepo productRepo;
    private final WebClient webClient;

    public String addProductToWishlist(final String buyerEmail, final String  productId) {
        final Optional<Wishlist> optionalWishlist = wishlistRepo.findById(buyerEmail);
        if (optionalWishlist.isEmpty()) {
            final ProductResponse productResponse = this.getProductDetails(productId);
            final Product product = convertProductResponseToProduct(productResponse);
            product.setBuyerEmail(buyerEmail);
            final Wishlist wishlist = new Wishlist();
            wishlist.setBuyerEmail(buyerEmail);
            wishlistRepo.save(wishlist);

            product.setWishlist(wishlist);
            productRepo.save(product);
            return "Product added to wishlist";
        }
        final ProductResponse productResponse = this.getProductDetails(productId);
        final Product product = convertProductResponseToProduct(productResponse);
        product.setBuyerEmail(buyerEmail);
        final Wishlist wishlist = optionalWishlist.get();
        product.setWishlist(wishlist);
        productRepo.save(product);
        return "Product added to wishlist";
    }

    public List<ProductResponse> getAllWishlistProducts(final String buyerEmail) {
        final Optional<Wishlist> optionalWishlist = wishlistRepo.findById(buyerEmail);
        if (optionalWishlist.isPresent()) {
            final Set<Product> products = productRepo.findByBuyerEmail(buyerEmail);
            return products.stream().map(this::convertProductToProductResponse).toList();
        }
        return List.of();
    }

    public Boolean deleteProductFromWishlist(final ProductId productId) {
        final Optional<Wishlist> optionalWishlist = wishlistRepo.findById(productId.getBuyerEmail());
        if (optionalWishlist.isPresent()) {
            final Set<Product> wishListedProducts = productRepo.findByBuyerEmail(productId.getBuyerEmail());
            final int wishListedProductsCount = wishListedProducts.size();
            if (wishListedProductsCount == 1) {
                productRepo.deleteById(productId);
                wishlistRepo.deleteById(productId.getBuyerEmail());
                return true;
            }

            final Optional<Product> optionalProduct = productRepo.findById(productId);
            if (optionalProduct.isPresent()) {
                productRepo.deleteById(productId);
                return true;
            }
        }
        return false;
    }

    private ProductResponse getProductDetails(final String  productId) {
        return webClient.get()
                .uri("http://localhost:8083/api/product/sku-code/{sku-code}", productId)
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
