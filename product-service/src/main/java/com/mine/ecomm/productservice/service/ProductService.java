package com.mine.ecomm.productservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mine.ecomm.productservice.dto.InventoryResponse;
import com.mine.ecomm.productservice.dto.ProductDTO;
import com.mine.ecomm.productservice.entity.Product;
import com.mine.ecomm.productservice.entity.ProductSellerDetail;
import com.mine.ecomm.productservice.repository.ProductRepository;

import jakarta.annotation.Nonnull;
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
    public void addNewProduct(@Nonnull final ProductDTO productDTO) {
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
                    updateSellerDetail(oldSellerDetails, productDTO);
                    product.getProductSellerDetails().set(i, oldSellerDetails);
                    product.setQuantity(product.getQuantity() + productDTO.getQuantity());
                    isSellerAlreadyExist = true;
                }
            }
            // add product with new seller fields (Eg: price, discount, quantity)
            if (!isSellerAlreadyExist) {
                addNewSellerToProduct(product, productDTO);
            }
        } else {
            product = addNewProductToCatalog(productDTO);
        }
        productRepository.save(product);
    }

    private Product addNewProductToCatalog(@Nonnull final ProductDTO productDTO) {
        final ProductSellerDetail newProductSellerDetail = new ProductSellerDetail();
        updateSellerDetail(newProductSellerDetail, productDTO);
        final List<ProductSellerDetail> productSellersDetails = new ArrayList<>();
        productSellersDetails.add(newProductSellerDetail);
        final Product newProduct = new Product();
        newProduct.setSkuCode(UUID.randomUUID().toString());
        newProduct.setProductName(productDTO.getProductName());
        newProduct.setCategory(productDTO.getCategory());
        newProduct.setShortDescription(productDTO.getShortDescription());
        newProduct.setDescription(productDTO.getDescription());
        newProduct.setProductSellerDetails(productSellersDetails);
        newProduct.setQuantity(productDTO.getQuantity());
        return newProduct;
    }

    private void addNewSellerToProduct(@Nonnull final Product product, @Nonnull final ProductDTO productDTO) {
        final ProductSellerDetail newSellerDetail = new ProductSellerDetail();
        updateSellerDetail(newSellerDetail, productDTO);
        product.getProductSellerDetails().add(newSellerDetail);
        product.setQuantity(product.getQuantity() + productDTO.getQuantity());
    }

    private void updateSellerDetail(@Nonnull final ProductSellerDetail sellerDetail, @Nonnull final ProductDTO productDTO) {
        sellerDetail.setProductPrice(productDTO.getProductPrice());
        sellerDetail.setDiscount(productDTO.getDiscount());
        final double newSellerEffectivePrice = calculateEffectivePrice(productDTO.getProductPrice(), productDTO.getDiscount());
        sellerDetail.setEffectivePrice(newSellerEffectivePrice);
        if (sellerDetail.getQuantity() == null || sellerDetail.getQuantity() == 0) {
            sellerDetail.setQuantity(productDTO.getQuantity());
        } else {
            sellerDetail.setQuantity(sellerDetail.getQuantity() + productDTO.getQuantity());
        }
        sellerDetail.setSellerEmail(productDTO.getSellerEmail());
    }

    /**
     * Gets all products.
     *
     * @return the all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<ProductDTO> searchProductByProductName(final String productName) {
        final List<Product> productList = productRepository.searchProductByProductNameContainingIgnoreCase(productName);
        return productList.stream().map(ProductDTO::new).toList();
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

    private static double calculateEffectivePrice(double price, double discount) {
        return price - (price * discount / 100);
    }
}
