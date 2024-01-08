package com.mine.ecomm.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mine.ecomm.inventoryservice.entity.Inventory;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findBySkuCode(final String skuCode);
}
