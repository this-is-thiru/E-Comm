package com.mine.ecomm.productservice.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mine.ecomm.productservice.dto.ProductDTO;
import com.mine.ecomm.productservice.entity.Product;
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
     * @param productDTO the product dto
     */
    @PostMapping("/add")
    public ResponseEntity<String> addProduct(final @RequestBody ProductDTO productDTO) {
        productService.addNewProduct(productDTO);
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
    public ResponseEntity<ProductDTO> getProduct(@PathVariable String productName) {
        final ProductDTO product = productService.getProduct(productName);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/{productName}/{sellerEmail}")
    public ResponseEntity<ProductDTO> getProductWithSeller(@PathVariable String productName, @PathVariable String sellerEmail) {
        final ProductDTO product = productService.getProductWithSeller(productName,sellerEmail);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
