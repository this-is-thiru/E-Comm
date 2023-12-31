package com.mine.ecomm.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mine.ecomm.productservice.entity.ProductPriceDetail;

public interface ProductPriceDetailRepo extends MongoRepository<ProductPriceDetail, String> {
}
