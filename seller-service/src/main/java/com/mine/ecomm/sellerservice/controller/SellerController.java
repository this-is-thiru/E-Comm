package com.mine.ecomm.sellerservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mine.ecomm.sellerservice.dto.ProductDTO;
import com.mine.ecomm.sellerservice.service.SellerService;

@CrossOrigin
@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    @PostMapping("/addProduct")
    public ResponseEntity<String> addProduct(final @RequestBody ProductDTO productDTO) {
        final String message = sellerService.addNewProduct(productDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<String> updateProductDetails(final @RequestBody ProductDTO productDTO) {
        final String message = sellerService.updateProductDetails(productDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/allProducts/{email}")
    public ResponseEntity<List<ProductDTO>> allSellerProducts(final @PathVariable String email) {
        final List<ProductDTO> allProducts = sellerService.getAllSellerProducts(email);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @PostMapping("/rate/{seller}")
    public ResponseEntity<String> rateTheSeller(final @PathVariable String seller, final @RequestParam("rate") int rating) {
        final String message = sellerService.rateTheSeller(seller, rating);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
