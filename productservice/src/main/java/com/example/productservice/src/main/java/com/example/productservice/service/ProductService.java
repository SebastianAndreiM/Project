package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Pattern: Service Layer
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return repo.findById(id);
    }

    public List<Product> searchByModel(String model) {
        return repo.findByModelContainingIgnoreCase(model);
    }

    public List<Product> searchByProducer(String producer) {
        return repo.findByProducerContainingIgnoreCase(producer);
    }

    public List<Product> searchByCategory(String category) {
        return repo.findByCategoryContainingIgnoreCase(category);
    }

    public List<Product> filterByPrice(double min, double max) {
        return repo.findByPriceBetween(min, max);
    }

    public List<Product> sortByPriceAsc() {
        return repo.findAllByOrderByPriceAsc();
    }

    public List<Product> sortByPriceDesc() {
        return repo.findAllByOrderByPriceDesc();
    }

    public Product saveProduct(Product product) {
        return repo.save(product);
    }

    public void deleteProduct(Long id) {
        repo.deleteById(id);
    }
}
