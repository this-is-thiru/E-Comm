package com.mine.ecomm.sellerservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.mine.ecomm.sellerservice.dto.ProductDTO;
import com.mine.ecomm.sellerservice.dto.SellerRateRequest;
import com.mine.ecomm.sellerservice.dto.SellerRateResponse;
import com.mine.ecomm.sellerservice.entity.Product;
import com.mine.ecomm.sellerservice.entity.SellerRating;
import com.mine.ecomm.sellerservice.exception.MetadataException;
import com.mine.ecomm.sellerservice.repository.SellerCatalogRepository;
import com.mine.ecomm.sellerservice.repository.SellerRateRepository;

import lombok.RequiredArgsConstructor;

/**
 * The type Seller service.
 */
@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerCatalogRepository sellerCatalogRepository;
    private final SellerRateRepository sellerRateRepository;
    private final WebClient webClient;
    private final RestTemplateProvider restTemplateProvider;

    /**
     * Add new product.
     *
     * @param productDTO the product dto
     */
    public String addNewProduct(final ProductDTO productDTO) {
        final Product entity = new Product();
        entity.setProductName(productDTO.getProductName());
        entity.setProductPrice(productDTO.getProductPrice());
        entity.setDiscount(productDTO.getDiscount());
        entity.setCategory(productDTO.getCategory());
        entity.setShortDescription(productDTO.getShortDescription());
        entity.setDescription(productDTO.getDescription());
        entity.setSellerEmail(productDTO.getSellerEmail());
        sellerCatalogRepository.saveAndFlush(entity);

        final String response = webClient.post()
                .uri("http://localhost:8083/api/product/add")
                .bodyValue(BodyInserters.fromValue(entity))
                .retrieve()
                .onStatus(HttpStatusCode::isError, ClientResponse::createException)
                .bodyToMono(String.class)
                .block();

//        final ResponseEntity<String> response = restTemplateProvider.post(entity);
        if (response == null) {
            throw new MetadataException("Failed to add product to all products.");
        }
        return "Product added successfully.";
    }

    /**
     * Update product.
     *
     * @param productDTO the product dto
     */
    public String updateProductDetails(final ProductDTO productDTO) {
        final Optional<Product> optionalProduct = sellerCatalogRepository.findById(productDTO.getProductId());

        // Checking if the same product is already present in seller catalog or not.
        if (optionalProduct.isPresent()) {
            final Product product = optionalProduct.get();
            // If same product is already present, we are updating the details.
            if (product.getSellerEmail().equals(productDTO.getSellerEmail())) {
                product.setProductPrice(productDTO.getProductPrice());
                product.setDiscount(productDTO.getDiscount());
                sellerCatalogRepository.saveAndFlush(product);
                final ResponseEntity<String> response = restTemplateProvider.post(product);
                if (response == null) {
                    throw new MetadataException("Failed to add product to all products.");
                }
                return "Product details are updated.";
            }
        }
        return "Product details are not updated (product is not found to update).";
    }

    /**
     * Gets sellers all products.
     *
     * @param email the email
     * @return the sellers all products
     */
    public List<ProductDTO> getAllSellerProducts(final String email) {
        final List<Product> sellerProducts = sellerCatalogRepository.findBySellerEmail(email);
        final List<ProductDTO> allProducts = new ArrayList<>();
        for (Product product : sellerProducts) {
            allProducts.add(new ProductDTO(product));
        }
        return allProducts;
    }

    public String rateTheSeller(final SellerRateRequest sellerRateRequest) {
        final String sellerEmail = sellerRateRequest.getSellerEmail();
        final Optional<SellerRating> optSellerRating = sellerRateRepository.findById(sellerEmail);
        if (optSellerRating.isEmpty()) {
            final SellerRating sellerRate = new SellerRating();
            sellerRate.setSellerEmail(sellerEmail);
            sellerRate.setSellerName(sellerRateRequest.getSellerName());
            sellerRate.setRating(sellerRateRequest.getRating());
            sellerRateRepository.saveAndFlush(sellerRate);
        } else {
            final SellerRating seller = optSellerRating.get();
            float oldRating = seller.getRating();
            final float newRating = (oldRating + sellerRateRequest.getRating()) / 2;
            seller.setRating(newRating);
            sellerRateRepository.saveAndFlush(seller);
        }
        return "Rating successfully noted.";
    }

    public List<SellerRateResponse> getAllSellersRating(final List<String> sellerEmails) {
        return sellerRateRepository.findAllBySellerEmailIn(sellerEmails).stream()
                .map(this::createSellerRateResponse).toList();
    }

    public SellerRateResponse getSellerRating(final String sellerEmail) {
        return sellerRateRepository.findById(sellerEmail).map(this::createSellerRateResponse).orElse(null);

    }

    private SellerRateResponse createSellerRateResponse(final SellerRating sellerRating) {
        final SellerRateResponse sellerRateResponse = new SellerRateResponse();
        sellerRateResponse.setRating(sellerRating.getRating());
        sellerRateResponse.setSellerEmail(sellerRating.getSellerEmail());
        sellerRateResponse.setSellerName(sellerRating.getSellerName());
        final Integer deliveryCharge = sellerRating.getDeliveryCharge();
        sellerRateResponse.setDeliveryCharge(deliveryCharge == null ? 0 : deliveryCharge);
        return sellerRateResponse;
    }

}
