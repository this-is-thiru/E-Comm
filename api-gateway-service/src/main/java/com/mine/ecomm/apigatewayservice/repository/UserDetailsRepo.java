package com.mine.ecomm.apigatewayservice.repository;


import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.mine.ecomm.apigatewayservice.entity.UserDetailsEntity;

@Repository
public interface UserDetailsRepo extends R2dbcRepository<UserDetailsEntity, String> {
}
