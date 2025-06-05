package com.example.productservice.repository;

import com.example.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // Pattern: Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByModelContainingIgnoreCase(String model);
    List<Product> findByProducerContainingIgnoreCase(String producer);
    List<Product> findByCategoryContainingIgnoreCase(String category);
    List<Product> findByPriceBetween(double min, double max);
    List<Product> findAllByOrderByPriceAsc();
    List<Product> findAllByOrderByPriceDesc();
}
