package com.mine.ecomm.sellerservice.controller;

import java.util.List;

import com.mine.ecomm.sellerservice.dto.SellerRateRequest;
import com.mine.ecomm.sellerservice.dto.SellerRateResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mine.ecomm.sellerservice.dto.ProductDTO;
import com.mine.ecomm.sellerservice.service.SellerService;

import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/api/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/product/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String addProduct(final @RequestBody ProductDTO productDTO) {
        return sellerService.addNewProduct(productDTO);
    }

    @PutMapping("/product/update")
    public ResponseEntity<String> updateProductDetails(final @RequestBody ProductDTO productDTO) {
        final String message = sellerService.updateProductDetails(productDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/product/{email}")
    public ResponseEntity<List<ProductDTO>> allSellerProducts(final @PathVariable String email) {
        final List<ProductDTO> allProducts = sellerService.getAllSellerProducts(email);
        return new ResponseEntity<>(allProducts, HttpStatus.OK);
    }

    @PostMapping("/rate")
    public ResponseEntity<String> rateTheSeller(final @RequestBody SellerRateRequest sellerRateRequest) {
        final String message = sellerService.rateTheSeller(sellerRateRequest);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/sellers-rating")
    @ResponseStatus(HttpStatus.OK)
    public List<SellerRateResponse> getAllSellersRating(final @RequestParam List<String> seller) {
        return sellerService.getAllSellersRating(seller);
    }

    @GetMapping("/seller-rating")
    @ResponseStatus(HttpStatus.OK)
    public SellerRateResponse getSellerRating(final @RequestParam String seller) {
        return sellerService.getSellerRating(seller);
    }
}
