package com.mine.ecomm.productservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mine.ecomm.productservice.dto.InventoryResponse;
import com.mine.ecomm.productservice.dto.ProductDTO;
import com.mine.ecomm.productservice.entity.Product;
import com.mine.ecomm.productservice.entity.ProductSellerDetail;
import com.mine.ecomm.productservice.repository.ProductRepository;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    /**
     * Add product.
     *
     * @param productDTO the product dto
     */
    public void addNewProduct(final ProductDTO productDTO) {
        final Optional<Product> productOptional = productRepository.findByProductName(productDTO.getProductName());
        final Product product;
        if (productOptional.isPresent()) {
            log.info("Product with name {} already exists", productDTO.getProductName());
            product = productOptional.get();
            final List<ProductSellerDetail> productSellersDetails = product.getProductSellerDetails();
            boolean isSellerAlreadyExist = false;
            for (int i = 0; i < productSellersDetails.size(); i++) {
                final ProductSellerDetail oldSellerDetails = productSellersDetails.get(i);
                if (productDTO.getSellerEmail().equals(oldSellerDetails.getSellerEmail())) {
                    oldSellerDetails.setProductPrice(productDTO.getProductPrice());
                    oldSellerDetails.setDiscount(productDTO.getDiscount());
                    final Long oldQuantity = oldSellerDetails.getQuantity();
                    oldSellerDetails.setQuantity(oldQuantity + productDTO.getQuantity());
                    product.getProductSellerDetails().set(i, oldSellerDetails);
                    product.setQuantity(product.getQuantity() + productDTO.getQuantity());
                    isSellerAlreadyExist = true;
                }
            }
            // add product with new seller fields (Eg: price, discount, quantity)
            if (!isSellerAlreadyExist) {
                updateProductDetailsWithSellers(product, productDTO);
            }
        } else {
            product = updateProductDetailsWithSellers(null, productDTO);
        }
        productRepository.save(product);
    }

    private Product updateProductDetailsWithSellers(@Nullable final Product product, @Nonnull final ProductDTO productDTO) {
        final ProductSellerDetail newSellerDetail = new ProductSellerDetail();
        newSellerDetail.setProductPrice(productDTO.getProductPrice());
        newSellerDetail.setDiscount(productDTO.getDiscount());
        newSellerDetail.setQuantity(productDTO.getQuantity());
        newSellerDetail.setSellerEmail(productDTO.getSellerEmail());
        if (product == null) {
            final List<ProductSellerDetail> productSellersDetails = new ArrayList<>();
            productSellersDetails.add(newSellerDetail);
            final Product newProduct = new Product();
            newProduct.setSkuCode(UUID.randomUUID().toString());
            newProduct.setProductName(productDTO.getProductName());
            newProduct.setCategory(productDTO.getCategory());
            newProduct.setShortDescription(productDTO.getShortDescription());
            newProduct.setDescription(productDTO.getDescription());
            newProduct.setProductSellerDetails(productSellersDetails);
            newProduct.setQuantity(productDTO.getQuantity());
            return newProduct;
        } else {
            product.getProductSellerDetails().add(newSellerDetail);
            product.setQuantity(product.getQuantity() + productDTO.getQuantity());
            return product;
        }
    }

    /**
     * Gets all products.
     *
     * @return the all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public ProductDTO getProduct(final String productName) {
        final Optional<Product> optionalProduct = productRepository.findByProductName(productName);
        return optionalProduct.map(ProductDTO::new).orElse(null);
    }

    public ProductDTO getProductWithSeller(final String productName, final String sellerEmail) {
        final Optional<Product> optionalProduct = productRepository.findByProductNameAndSellerEmail(productName, sellerEmail);
        return optionalProduct.map(ProductDTO::new).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        return productRepository.findBySkuCodes(skuCodes).stream()
                .map(product ->
                        InventoryResponse.builder()
                                .skuCode(product.getSkuCode())
                                .isInStock(product.getQuantity() > 0)
                                .build()
                ).toList();
    }
}
