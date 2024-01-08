package com.mine.ecomm.inventoryservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mine.ecomm.inventoryservice.dto.InventoryResponse;
import com.mine.ecomm.inventoryservice.entity.Inventory;
import com.mine.ecomm.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodes) {
        return inventoryRepository.findBySkuCodeIn(skuCodes).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }

    public boolean addStockQuantity(String skuCode, int quantity) {
        final Optional<Inventory> OptInventory = inventoryRepository.findBySkuCode(skuCode);
        if (OptInventory.isPresent()) {
            final Inventory inventory = OptInventory.get();
            inventory.setQuantity(inventory.getQuantity() + quantity);
            inventoryRepository.saveAndFlush(inventory);
            return true;
        }
        final Inventory inventory = Inventory.builder()
                .skuCode(skuCode)
                .quantity(quantity)
                .build();
        inventoryRepository.saveAndFlush(inventory);
        return true;
    }
}
