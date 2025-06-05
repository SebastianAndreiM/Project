package com.example.inventoryservice.service;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Pattern: Service Layer
public class InventoryService {
    private final InventoryRepository repo;

    public InventoryService(InventoryRepository repo) {
        this.repo = repo;
    }

    public List<Inventory> getAllInventories() {
        return repo.findAll();
    }

    public Optional<Inventory> getInventoryById(Long id) {
        return repo.findById(id);
    }

    public List<Inventory> getByStore(Long storeId) {
        return repo.findByStoreId(storeId);
    }

    public List<Inventory> getByProduct(Long productId) {
        return repo.findByProductId(productId);
    }

    public List<Inventory> getBySize(String size) {
        return repo.findBySize(size);
    }

    public List<Inventory> getByColor(String color) {
        return repo.findByColor(color);
    }

    public List<Inventory> getByLowStock(int stock) {
        return repo.findByStockLessThanEqual(stock);
    }

    public List<Inventory> getByStoreAndProduct(Long storeId, Long productId) {
        return repo.findByStoreIdAndProductId(storeId, productId);
    }

    public List<Inventory> sortByStock(String order) {
        if ("desc".equalsIgnoreCase(order)) {
            return repo.findAllByOrderByStockDesc();
        }
        return repo.findAllByOrderByStockAsc();
    }

    public List<Inventory> sortBySize() {
        return repo.findAllByOrderBySizeAsc();
    }

    public List<Inventory> sortByColor() {
        return repo.findAllByOrderByColorAsc();
    }

    public Inventory saveInventory(Inventory inventory) {
        return repo.save(inventory);
    }

    public void deleteInventory(Long id) {
        repo.deleteById(id);
    }
}
