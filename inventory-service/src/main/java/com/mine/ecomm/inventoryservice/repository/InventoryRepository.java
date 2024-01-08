package com.mine.ecomm.inventoryservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mine.ecomm.inventoryservice.entity.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findBySkuCode(final String skuCode);

    List<Inventory> findBySkuCodeIn(final List<String> skuCodes);
}
