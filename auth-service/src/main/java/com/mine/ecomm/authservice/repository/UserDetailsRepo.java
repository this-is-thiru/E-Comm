package com.mine.ecomm.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mine.ecomm.authservice.entity.UserDetailsEntity;

public interface UserDetailsRepo extends JpaRepository<UserDetailsEntity, String> {
}
