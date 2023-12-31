package com.mine.ecomm.productservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mine.ecomm.productservice.dto.ProductDTO;
import com.mine.ecomm.productservice.entity.Product;
import com.mine.ecomm.productservice.entity.ProductPriceDetail;
import com.mine.ecomm.productservice.repository.ProductPriceDetailRepo;
import com.mine.ecomm.productservice.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPriceDetailRepo priceDetailRepository;

    /**
     * Add product.
     *
     * @param productDTO the product dto
     */
    public void addProduct(final ProductDTO productDTO) {
        final Optional<Product> productOptional = productRepository.findByProductName(productDTO.getProductName());
        if (productOptional.isPresent()) {
            final Product entity = getProductFromProductDTO(productDTO, productOptional);

            final List<String> priceDetailIds = entity.getPriceDetailIds();

            boolean isUpdated = false;
            for (String priceDetailId: priceDetailIds) {
                final Optional<ProductPriceDetail> priceDetailOptional = priceDetailRepository.findById(priceDetailId);
                if (priceDetailOptional.isPresent()) {
                    final ProductPriceDetail priceDetail = priceDetailOptional.get();
                    if (productDTO.getSellerEmail().equals(priceDetail.getSellerEmail())) {
                        priceDetail.setProductPrice(productDTO.getProductPrice());
                        priceDetail.setDiscount(productDTO.getDiscount());
                        priceDetailRepository.save(priceDetail);
                        isUpdated = true;
                        break;
                    }
                }
            }
            if (!isUpdated) {
                updateProductDetails(productDTO, entity);
            }
        } else {
            addNewProduct(productDTO);
        }
    }

    private static Product getProductFromProductDTO(ProductDTO productDTO, Optional<Product> productOptional) {
        final Product entity = productOptional.orElseThrow(() -> new RuntimeException("product not found"));

        // updating product with lower prices to show initially
        boolean isEntityUpdated = false;
        if (entity.getProductPrice() > productDTO.getProductPrice()){
            entity.setProductPrice(productDTO.getProductPrice());
            isEntityUpdated = true;
        }
        if (entity.getDiscount() > productDTO.getDiscount()){
            entity.setDiscount(productDTO.getDiscount());
            isEntityUpdated = true;
        }
        if (isEntityUpdated) {
            entity.setSellerEmail(productDTO.getSellerEmail());
        }
        return entity;
    }

    /**
     * Add new product.
     *
     * @param productDTO the product dto
     */
    private void addNewProduct(final ProductDTO productDTO) {
        final Product entity = new Product();
        entity.setProductName(productDTO.getProductName());
        entity.setCategory(productDTO.getCategory());
        entity.setShortDescription(productDTO.getShortDescription());
        entity.setDescription(productDTO.getDescription());
        entity.setProductPrice(productDTO.getProductPrice());
        entity.setDiscount(productDTO.getDiscount());
        entity.setSellerEmail(productDTO.getSellerEmail());

        // updating seller details
        updateProductDetails(productDTO, entity);
    }

    private void updateProductDetails(ProductDTO productDTO, Product entity) {
        final ProductPriceDetail priceDetail = new ProductPriceDetail();
        priceDetail.setId(productDTO.getProductId());
        priceDetail.setProductPrice(productDTO.getProductPrice());
        priceDetail.setDiscount(productDTO.getDiscount());
        priceDetail.setSellerEmail(productDTO.getSellerEmail());
        ProductPriceDetail savedPriceDetail = priceDetailRepository.save(priceDetail);

        entity.addPriceDetailId(savedPriceDetail.getId());
        productRepository.save(entity);
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
        Optional<Product> optionalProduct = productRepository.findByProductName(productName);
        ProductDTO productWithSellers = null;
        if (optionalProduct.isPresent()){
            final Product product = optionalProduct.get();
            productWithSellers = new ProductDTO(product);
            final List<String> priceDetailIds = product.getPriceDetailIds();
            final List<ProductPriceDetail> priceDetails = new ArrayList<>(2);
            for (String i: priceDetailIds){
                Optional<ProductPriceDetail> optionalPriceDetail = priceDetailRepository.findById(i);
                priceDetails.add(optionalPriceDetail.orElse(null));
            }
            productWithSellers.setPriceDetails(priceDetails);
        }
        return productWithSellers;
    }
}
