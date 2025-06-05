package com.example.productservice;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductDataInitializer {
    @Bean
    CommandLineRunner initProducts(ProductRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Product("Nike Air Force 1", "Nike", "Sneakers", "Pantofi sport albi", "img/nike_af1.jpg", 499.99));
                repo.save(new Product("Adidas Superstar", "Adidas", "Casual", "Adidasi casual", "img/adidas_superstar.jpg", 399.99));
                repo.save(new Product("Puma RS-X", "Puma", "Sneakers", "Sneakers trendy", "img/puma_rsx.jpg", 350.00));
            }
        };
    }
}
