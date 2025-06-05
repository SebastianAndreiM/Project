package com.example.inventoryservice.repository;

import com.example.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Pattern: Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByStoreId(Long storeId);
    List<Inventory> findByProductId(Long productId);
    List<Inventory> findBySize(String size);
    List<Inventory> findByColor(String color);
    List<Inventory> findByStockLessThanEqual(int stock);
    List<Inventory> findByStoreIdAndProductId(Long storeId, Long productId);

    List<Inventory> findAllByOrderByStockAsc();
    List<Inventory> findAllByOrderByStockDesc();
    List<Inventory> findAllByOrderBySizeAsc();
    List<Inventory> findAllByOrderByColorAsc();
}
