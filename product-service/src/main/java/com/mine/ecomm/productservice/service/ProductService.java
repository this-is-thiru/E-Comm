package com.mine.ecomm.productservice.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.mine.ecomm.productservice.dto.*;
import com.mine.ecomm.productservice.entity.Product;
import com.mine.ecomm.productservice.entity.ProductSellerDetail;
import com.mine.ecomm.productservice.exception.ServiceException;
import com.mine.ecomm.productservice.repository.ProductRepository;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final WebClient webClient;

    /**
     * Add product.
     *
     * @param productRequest the product dto
     */
    public void addNewProduct(@Nonnull final ProductRequest productRequest) {
        final Optional<Product> productOptional = productRepository.findByProductName(productRequest.getProductName());
        final Product product;
        if (productOptional.isPresent()) {
            log.info("Product with name {} already exists", productRequest.getProductName());
            product = productOptional.get();
            final List<ProductSellerDetail> productSellersDetails = product.getProductSellerDetails();
            boolean isSellerAlreadyExist = false;
            for (int i = 0; i < productSellersDetails.size(); i++) {
                final ProductSellerDetail oldSellerDetails = productSellersDetails.get(i);
                if (productRequest.getSellerEmail().equals(oldSellerDetails.getSellerEmail())) {
                    updatePriceForSeller(oldSellerDetails, productRequest);
                    product.getProductSellerDetails().set(i, oldSellerDetails);
                    product.setQuantity(product.getQuantity() + productRequest.getQuantity());
                    isSellerAlreadyExist = true;
                }
            }
            // add product with new seller fields (Eg: price, discount, quantity)
            if (!isSellerAlreadyExist) {
                addNewSellerToProduct(product, productRequest);
            }
        } else {
            product = addNewProductToCatalog(productRequest);
        }
        productRepository.save(product);
    }

    private Product addNewProductToCatalog(@Nonnull final ProductRequest productRequest) {
        final ProductSellerDetail newProductSellerDetail = new ProductSellerDetail();
        updatePriceForSeller(newProductSellerDetail, productRequest);
        final List<ProductSellerDetail> productSellersDetails = new ArrayList<>();
        productSellersDetails.add(newProductSellerDetail);
        final Product newProduct = new Product();
        newProduct.setSkuCode(UUID.randomUUID().toString());
        newProduct.setProductName(productRequest.getProductName());
        newProduct.setCategory(productRequest.getCategory());
        newProduct.setShortDescription(productRequest.getShortDescription());
        newProduct.setDescription(productRequest.getDescription());
        newProduct.setProductSellerDetails(productSellersDetails);
        newProduct.setQuantity(productRequest.getQuantity());
        return newProduct;
    }

    private void addNewSellerToProduct(@Nonnull final Product product, @Nonnull final ProductRequest productRequest) {
        final ProductSellerDetail newSellerDetail = new ProductSellerDetail();
        updatePriceForSeller(newSellerDetail, productRequest);
        product.getProductSellerDetails().add(newSellerDetail);
        product.setQuantity(product.getQuantity() + productRequest.getQuantity());
    }

    private void updatePriceForSeller(@Nonnull final ProductSellerDetail sellerDetail, @Nonnull final ProductRequest productRequest) {
        sellerDetail.setProductPrice(productRequest.getProductPrice());
        sellerDetail.setDiscount(productRequest.getDiscount());
        final double newSellerEffectivePrice = calculateEffectivePrice(productRequest.getProductPrice(), productRequest.getDiscount());
        sellerDetail.setEffectivePrice(newSellerEffectivePrice);
        if (sellerDetail.getQuantity() == null || sellerDetail.getQuantity() == 0) {
            sellerDetail.setQuantity(productRequest.getQuantity());
        } else {
            sellerDetail.setQuantity(sellerDetail.getQuantity() + productRequest.getQuantity());
        }
        sellerDetail.setSellerEmail(productRequest.getSellerEmail());
    }

    /**
     * Gets all products.
     *
     * @return the all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<ProductResponse> searchProductByProductName(final String productName) {
        final List<Product> productList = productRepository.searchProductByProductNameContainingIgnoreCase(productName);
        return productList.stream().map(this::createProductResponse).toList();
    }

    public ProductResponse getProductWithSeller(final String productName, final String sellerEmail) {
        final Optional<Product> optionalProduct = productRepository.findByProductNameAndSellerEmail(productName, sellerEmail);
        return optionalProduct.map(this::createProductResponse).orElse(null);
    }


    public List<SellerDetail> getAllSellersForProduct(final String skuCode) throws ServiceException {
        final Optional<Product> optionalProduct = productRepository.findBySkuCode(skuCode);
        if (optionalProduct.isPresent()) {
            final List<ProductSellerDetail> productSellerDetails = optionalProduct.get().getProductSellerDetails();
            final Map<String, List<ProductSellerDetail>> sellerMap = productSellerDetails.stream()
                    .collect(Collectors.groupingBy(ProductSellerDetail::getSellerEmail));

            final List<String> sellerEmails = sellerMap.keySet().stream().toList();

            // No sellers found for the product
            if (sellerEmails.isEmpty()) {
                return Collections.emptyList();
            }

            // Seller name and delivery charge and rating is fetched from seller service
            final SellerRateResponse[] ratesResponse = webClient.get()
                    .uri("http://localhost:8082/api/seller/sellers-rating/{seller}",
                                uriBuilder -> uriBuilder.build(sellerEmails))
                    .retrieve()
                    .bodyToMono(SellerRateResponse[].class)
                    .block();

            if (ratesResponse == null) {
                throw new ServiceException("Seller details not found in database.", HttpStatus.NO_CONTENT);
            }
            final List<SellerDetail> sellerDetails = new ArrayList<>();
            for (SellerRateResponse rateResponse : ratesResponse) {
                final SellerDetail sellerDetail = new SellerDetail();
                final String sellerEmail = rateResponse.getSellerEmail();
                final ProductSellerDetail productSellerDetail = sellerMap.get(sellerEmail).get(0);

                sellerDetail.setSellerEmail(sellerEmail);
                sellerDetail.setProductPrice(productSellerDetail.getProductPrice());
                sellerDetail.setDiscount(productSellerDetail.getDiscount());
                sellerDetail.setEffectivePrice(productSellerDetail.getEffectivePrice());
                sellerDetail.setRating(rateResponse.getRating());
                sellerDetail.setDeliveryCharge(rateResponse.getDeliveryCharge());
                sellerDetail.setSellerName(rateResponse.getSellerName());

                sellerDetails.add(sellerDetail);
            }
            return sellerDetails;
        }
        // No product found
        throw new ServiceException("No product found for sku code " + skuCode, HttpStatus.NOT_FOUND);
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

    private ProductResponse createProductResponse(Product product) {
        final ProductSellerDetail minPriceSeller = product.getProductSellerDetails().stream()
                .filter(psd -> psd.getQuantity() > 0)
                .min(Comparator.comparing(ProductSellerDetail::getEffectivePrice))
                .orElse(new ProductSellerDetail());

        final ProductResponse productResponse = new ProductResponse();
        productResponse.setSkuCode(product.getSkuCode());
        productResponse.setProductName(product.getProductName());
        productResponse.setCategory(product.getCategory());
        productResponse.setShortDescription(product.getShortDescription());
        productResponse.setDescription(product.getDescription());
        productResponse.setProductPrice(minPriceSeller.getProductPrice());
        productResponse.setDiscount(minPriceSeller.getDiscount());
        productResponse.setEffectivePrice(minPriceSeller.getEffectivePrice());
        return productResponse;
    }

    private static double calculateEffectivePrice(double price, double discount) {
        return price - (price * discount / 100);
    }
}
