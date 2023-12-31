package com.mine.ecomm.orderservice.service;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Rest template provider.
 */
@Component
public class RestTemplateProvider {


    /**
     * Post response entity.
     *
     * @param sellerEmail the seller email
     * @param rate        the rate
     * @return the response entity
     */
    public ResponseEntity<String> post(final String sellerEmail, final int rate) {
        String url = "http://localhost:8200/seller/rate/{seller}";
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("seller", sellerEmail);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("rate", rate);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(builder.buildAndExpand(uriVariables).toUri(), HttpMethod.POST, null, String.class);
    }
}