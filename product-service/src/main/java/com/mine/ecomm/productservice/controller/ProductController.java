package com.mine.ecomm.productservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mine.ecomm.productservice.dto.InventoryResponse;
import com.mine.ecomm.productservice.dto.ProductRequest;
import com.mine.ecomm.productservice.dto.ProductResponse;
import com.mine.ecomm.productservice.entity.Product;
import com.mine.ecomm.productservice.exception.ProductServiceException;
import com.mine.ecomm.productservice.service.ProductService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @RequestMapping(value="/")
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }

    /**
     * Add product.
     *
     * @param productRequest the product dto
     */
    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody final ProductRequest productRequest) {
        productService.addNewProduct(productRequest);
        return new ResponseEntity<>("product added successfully.", HttpStatus.OK);
    }

    /**
     * Gets all products.
     *
     * @return the all products
     */
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        final List<Product> productList = productService.getAllProducts();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }


    /**
     * Gets product.
     *
     * @param productName the product name
     *
     * @return the product
     */
    @GetMapping("/{productName}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> searchProductByProductName(@PathVariable String productName) {
        final List<ProductResponse> product = productService.searchProductByProductName(productName);
        if (product.isEmpty()) {
            throw new ProductServiceException("No product found with name: " + productName + ".", HttpStatus.NOT_FOUND);
        }
        return product;
    }

    @GetMapping("/{productName}/{sellerEmail}")
    public ResponseEntity<ProductResponse> getProductWithSeller(@PathVariable String productName, @PathVariable String sellerEmail) {
        final ProductResponse product = productService.getProductWithSeller(productName,sellerEmail);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/in-stock")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(final @RequestParam List<String> skuCodes) {
        return productService.isInStock(skuCodes);
    }
}
