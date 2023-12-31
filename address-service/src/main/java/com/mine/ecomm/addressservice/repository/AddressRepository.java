package com.mine.ecomm.addressservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mine.ecomm.addressservice.entity.Address;

/**
 * The interface Address repository.
 */
public interface AddressRepository extends JpaRepository<Address, String> {
}
