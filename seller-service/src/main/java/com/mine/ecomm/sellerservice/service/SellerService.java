package com.mine.ecomm.sellerservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mine.ecomm.sellerservice.dto.ProductDTO;
import com.mine.ecomm.sellerservice.entity.Product;
import com.mine.ecomm.sellerservice.entity.SellerRating;
import com.mine.ecomm.sellerservice.exception.MetadataException;
import com.mine.ecomm.sellerservice.repository.SellerCatalogRepository;
import com.mine.ecomm.sellerservice.repository.SellerRateRepository;

/**
 * The type Seller service.
 */
@Service
public class SellerService {

    @Autowired
    private SellerCatalogRepository sellerCatalogRepository;

    @Autowired
    private SellerRateRepository sellerRateRepository;

    @Autowired
    private RestTemplateProvider restTemplateProvider;

    /**
     * Add new product.
     *
     * @param productDTO the product dto
     */
    public String addNewProduct(final ProductDTO productDTO) {
        final Product entity = new Product();
        entity.generateProductId(productDTO);
        entity.setProductName(productDTO.getProductName());
        entity.setProductPrice(productDTO.getProductPrice());
        entity.setDiscount(productDTO.getDiscount());
        entity.setCategory(productDTO.getCategory());
        entity.setShortDescription(productDTO.getShortDescription());
        entity.setDescription(productDTO.getDescription());
        entity.setSellerEmail(productDTO.getSellerEmail());
        sellerCatalogRepository.saveAndFlush(entity);

        final ResponseEntity<String> response = restTemplateProvider.post(entity);
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

    /**
     * Gets sellers all products.
     *
     * @param sellerEmail the email
     * @return the sellers all products
     */
    public String rateTheSeller(final String sellerEmail, final int rating) {
        final Optional<SellerRating> optSellerRating = sellerRateRepository.findById(sellerEmail);
        if (optSellerRating.isEmpty()) {
            final SellerRating sellerRate = new SellerRating();
            sellerRate.setSellerEmail(sellerEmail);
            sellerRate.setRating(rating);
            sellerRateRepository.saveAndFlush(sellerRate);
        } else {
            final SellerRating seller = optSellerRating.get();
            float sellerRating = seller.getRating();
            sellerRating = (sellerRating + rating) / 2;
            seller.setRating(sellerRating);
            sellerRateRepository.saveAndFlush(seller);
        }
        return "Rating successfully noted.";
    }

}
