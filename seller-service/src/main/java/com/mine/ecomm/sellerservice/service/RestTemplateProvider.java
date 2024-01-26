package com.mine.ecomm.sellerservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.mine.ecomm.sellerservice.entity.Product;

/**
 * The type Rest template provider.
 */
@Component
public class RestTemplateProvider {

    /**
     * Creating an instance of RestTemplate class
     * The Rest template.
     */
    RestTemplate restTemplate = new RestTemplate();

    /**
     * Post response entity.
     *
     * @param product the product
     *
     * @return the response entity
     */
    public ResponseEntity<String> post(Product product) {
        return restTemplate.postForEntity("http://localhost:8083/product/add", product, String.class, "");
    }
}